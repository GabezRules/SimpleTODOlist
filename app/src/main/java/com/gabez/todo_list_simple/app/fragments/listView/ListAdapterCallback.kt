package com.gabez.todo_list_simple.app.fragments.listView

import com.gabez.todo_list_simple.app.fragments.listView.list.ListViewPagedAdapter
import com.gabez.todo_list_simple.domain.entities.ItemTODO

interface ListAdapterCallback {
    var pagedAdapter: ListViewPagedAdapter

    fun deleteItem(item: ItemTODO)
    fun editItem(item: ItemTODO)
}