package com.gabez.todo_list_simple.app.fragments.listView.list

import com.gabez.todo_list_simple.domain.entities.ItemTODO

interface ListOptionsInterface {
    fun deleteItem(item: ItemTODO)
    fun editItem(item: ItemTODO)
}