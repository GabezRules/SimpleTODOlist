package com.gabez.todo_list_simple.app.fragments.listView.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.gabez.todo_list_simple.R
import com.gabez.todo_list_simple.app.fragments.listView.ListAdapterCallback
import com.gabez.todo_list_simple.domain.entities.ItemTODO

class ListViewPagedAdapter(private val callback: ListAdapterCallback):
    PagingDataAdapter<ItemTODO, ItemTODOViewHolder>(DIFF_CALLBACK), ListOptionsInterface {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemTODOViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_todo_single_view, parent, false)
        return ItemTODOViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: ItemTODOViewHolder, position: Int) {
        getItem(position)?.let { item ->
            viewHolder.bindView(item)

            viewHolder.singleItemContainer.setOnLongClickListener {
                deleteItem(item)
                true
            }

            viewHolder.singleItemContainer.setOnClickListener {
                editItem(item)
            }
        }
    }

    override fun editItem(item: ItemTODO) {
        callback.editItem(item)
    }

    override fun deleteItem(item: ItemTODO) {
        callback.deleteItem(item)
    }

    companion object{
        object DIFF_CALLBACK : DiffUtil.ItemCallback<ItemTODO>() {
            override fun areItemsTheSame(oldItem: ItemTODO, newItem: ItemTODO) =
                oldItem.ID == newItem.ID

            override fun areContentsTheSame(oldItem: ItemTODO, newItem: ItemTODO) =
                oldItem == newItem
        }
    }
}