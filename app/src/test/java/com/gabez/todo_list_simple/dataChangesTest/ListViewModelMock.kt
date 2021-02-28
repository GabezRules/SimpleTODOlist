package com.gabez.todo_list_simple.dataChangesTest

import com.gabez.todo_list_simple.data.firestore.responses.DataChangedMessage
import com.gabez.todo_list_simple.domain.entities.ItemTODO
import com.google.firebase.firestore.DocumentChange

class ListViewModelMock {
    var data: ArrayList<ItemTODO> = ArrayList()

    fun updateItemsPostChange(changesList: ArrayList<DataChangedMessage>){
        for (item in changesList) {
            when (item.status) {
                DocumentChange.Type.ADDED -> data.add(item.data)
                DocumentChange.Type.MODIFIED -> {

                    var itemIndex: Int? = null
                    data.map { dataItem ->
                        if (dataItem.ID == item.data.ID) itemIndex = data.indexOf(dataItem)
                    }
                    itemIndex?.let { data[it] = item.data }

                }
                DocumentChange.Type.REMOVED -> data.remove(item.data)
            }
        }
    }
}