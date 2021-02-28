package com.gabez.todo_list_simple.data.dataSources

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemSnapshotList
import com.gabez.todo_list_simple.app.fragments.listView.list.ListViewPagedAdapter
import com.gabez.todo_list_simple.data.firestore.responses.DataChangedMessage
import com.gabez.todo_list_simple.domain.entities.ItemTODO
import com.google.firebase.firestore.DocumentChange

class RemoteDataChangeProvider {
    private val _localDownloadedData: MutableLiveData<ItemSnapshotList<ItemTODO>> = MutableLiveData()
    private val _dataFromServer: MutableLiveData<List<ItemTODO>> = MutableLiveData()

    val dataChanges: MutableLiveData<ArrayList<DataChangedMessage>> = MutableLiveData(ArrayList())

    fun saveLocalData(newData: ItemSnapshotList<ItemTODO>) {
        _localDownloadedData.postValue(newData)
    }

    fun saveRemoteDataAndCompare(newData: List<ItemTODO>) {
        _dataFromServer.postValue(newData)
        val changesForCommonData = compareCommon()
        val changesForNewAndDeletedData = findNewAndDeleted()

        if (changesForCommonData.size > 0 || changesForNewAndDeletedData.size > 0) {
            val changesList: ArrayList<DataChangedMessage> =
                changesForCommonData.clone() as ArrayList<DataChangedMessage>
            changesList.addAll(changesForNewAndDeletedData)

            dataChanges.postValue(changesList)
        }

    }

    private fun findNewAndDeleted(): ArrayList<DataChangedMessage> {
        val changes: ArrayList<DataChangedMessage> = ArrayList()
        val localDataArray: ArrayList<ItemTODO> = ArrayList()

        _localDownloadedData.value?.let {
            it.map { itemTODO -> localDataArray.add(itemTODO!!) }
        }

        _dataFromServer.value?.let {
            for (item in it) {

                if (item !in localDataArray) {
                    changes.add(
                        DataChangedMessage(
                            DocumentChange.Type.ADDED,
                            item
                        )
                    )

                    Log.v("DB", "ADD: "+item.description)
                }
            }
        }

        for (item in localDataArray) {
            if (item !in _dataFromServer.value!!) {
                changes.add(
                    DataChangedMessage(
                        DocumentChange.Type.REMOVED,
                        item
                    )
                )
                Log.v("DB", "REMOVED")
            }
        }

        return changes
    }

    private fun compareCommon(): ArrayList<DataChangedMessage> {
        val changes: ArrayList<DataChangedMessage> = ArrayList()

        _localDownloadedData.value!!.map { localItem ->
            for (remoteItem in _dataFromServer.value!!) {
                if (localItem!!.ID == remoteItem.ID && localItem != remoteItem) {
                    changes.add(DataChangedMessage(DocumentChange.Type.MODIFIED, remoteItem))
                    Log.v("DB", "MODIFIED")
                }
            }
        }

        return changes
    }
}