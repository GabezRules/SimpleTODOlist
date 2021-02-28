package com.gabez.todo_list_simple.app.fragments.listView

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import androidx.paging.*
import com.gabez.todo_list_simple.data.pagination.PagingDatasource
import com.gabez.todo_list_simple.data.dataSources.RemoteDataChangeProvider
import com.gabez.todo_list_simple.data.firestore.responses.ApiResponseStatus
import com.gabez.todo_list_simple.data.firestore.responses.DataChangedMessage
import com.gabez.todo_list_simple.domain.entities.ItemTODO
import com.gabez.todo_list_simple.domain.usecases.DeleteItemUsecase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ListViewModel(
    private val deleteItemUsecase: DeleteItemUsecase,
    private val context: Context,
    private val pagingDatasource: PagingDatasource,
    private val remoteDataChangeProvider: RemoteDataChangeProvider
) : ViewModel() {

    val dataChanges: LiveData<ArrayList<DataChangedMessage>> = remoteDataChangeProvider.dataChanges

    val flow: Flow<PagingData<ItemTODO>>
        get() = Pager(PagingConfig(30)) {
            pagingDatasource
        }.flow.cachedIn(viewModelScope)

    fun deleteItem(item: ItemTODO) = viewModelScope.launch {
        deleteItemUsecase.invoke(item).collect { response ->
            when (response.status) {
                ApiResponseStatus.SUCCESS -> {
                    showToast("item deleted")
                }
                ApiResponseStatus.FAILED -> response.data?.let {
                    showToast(it.toString())
                }
            }
        }
    }

    private fun showToast(text: String) {
        GlobalScope.launch(Dispatchers.Main) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }
}