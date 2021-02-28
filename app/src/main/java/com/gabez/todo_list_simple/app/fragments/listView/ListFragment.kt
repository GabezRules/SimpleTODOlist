package com.gabez.todo_list_simple.app.fragments.listView

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gabez.todo_list_simple.R
import com.gabez.todo_list_simple.app.fragments.listView.list.ListViewPagedAdapter
import com.gabez.todo_list_simple.backgroundService.ObserveChangesService
import com.gabez.todo_list_simple.data.dataSources.RemoteDataChangeProvider
import com.gabez.todo_list_simple.domain.entities.ItemTODO
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentChange
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject


@InternalCoroutinesApi
class ListFragment : Fragment(), KoinComponent, ListAdapterCallback {

    val viewModel: ListViewModel by inject()
    val remoteDataChangeProvider: RemoteDataChangeProvider by inject()

    private lateinit var itemsRecyclerView: RecyclerView
    private lateinit var addItemButton: FloatingActionButton

    private lateinit var progressBar: ProgressBar
    private lateinit var progressBarLoadMore: ProgressBar

    private lateinit var btnExitApp: ExtendedFloatingActionButton

    override var pagedAdapter: ListViewPagedAdapter =
        ListViewPagedAdapter(this@ListFragment as ListAdapterCallback)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflater.inflate(R.layout.fragment_list, container, false).also { view ->
            initViews(view)

            setupAddButton()
            setupList()
            getLatestItems()
            setupExitAppButton()

            lifecycleScope.launch {
                pagedAdapter.loadStateFlow.collectLatest { loadStates ->

                    progressBar.visibility = when(loadStates.refresh){
                        is LoadState.NotLoading -> View.GONE
                        LoadState.Loading -> View.VISIBLE
                        is LoadState.Error -> View.GONE
                    }

                    itemsRecyclerView.visibility = when(loadStates.refresh){
                        is LoadState.NotLoading -> View.VISIBLE
                        LoadState.Loading -> View.GONE
                        is LoadState.Error -> View.VISIBLE
                    }

                    progressBarLoadMore.visibility = when(loadStates.append){
                        is LoadState.NotLoading -> View.GONE
                        LoadState.Loading -> View.VISIBLE
                        is LoadState.Error -> View.GONE
                    }

                }
            }

            observeDataChanges()

            return view
        }
    }

    private fun getLatestItems(){
        lifecycleScope.launch {
            viewModel.flow.collect { pagingData ->
                pagedAdapter.submitData(lifecycle, pagingData)
                pagedAdapter.notifyDataSetChanged()
                remoteDataChangeProvider.saveLocalData(pagedAdapter.snapshot())
            }
        }
    }

    private fun initViews(view: View) {
        itemsRecyclerView = view.findViewById(R.id.itemsRecyclerView)
        addItemButton = view.findViewById(R.id.addItemButton)
        progressBar = view.findViewById(R.id.progressBar)
        progressBarLoadMore = view.findViewById(R.id.progressBarLoadMore)
        btnExitApp = view.findViewById(R.id.btnExitApp)
    }

    private fun setupAddButton() {
        addItemButton.setOnClickListener {
            this@ListFragment.findNavController().navigate(R.id.open_addItemFragment)
        }
    }

    private fun setupList() {
        itemsRecyclerView.let {
            it.layoutManager = LinearLayoutManager(this@ListFragment.context)
            it.adapter = pagedAdapter
        }
    }

    override fun deleteItem(item: ItemTODO) {
        AlertDialog.Builder(this@ListFragment.requireContext())
            .setTitle("Delete item")
            .setMessage("Are you sure you want to delete this item?")
            .setPositiveButton("Yes, delete") { _, _ ->
                viewModel.deleteItem(item)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun editItem(item: ItemTODO) {
        this@ListFragment.findNavController().navigate(R.id.open_editItemFragment, bundleOf(Pair("baseItem", item)))
    }

    private fun observeDataChanges(){
        viewModel.dataChanges.observe(viewLifecycleOwner, Observer{
            dataChanges ->
            var shouldRefreshAll: Boolean = false
            Log.v("DB", dataChanges.size.toString())
            if(dataChanges.size > 0){
                Toast.makeText(requireContext(), "updating data", Toast.LENGTH_SHORT).show()
                for (item in dataChanges) {
                    when (item.status) {
                        DocumentChange.Type.ADDED -> shouldRefreshAll = true
                        DocumentChange.Type.MODIFIED -> pagedAdapter.editItem(item.data)
                        DocumentChange.Type.REMOVED -> pagedAdapter.deleteItem(item.data)
                    }
                }

                pagedAdapter.notifyDataSetChanged()
                if(shouldRefreshAll) getLatestItems()
            }
        })
    }

    private fun setupExitAppButton(){
        btnExitApp.setOnClickListener {
            ObserveChangesService.stopService(requireContext())
        }
    }
}