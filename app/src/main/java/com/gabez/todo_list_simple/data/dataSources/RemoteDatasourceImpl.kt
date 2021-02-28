package com.gabez.todo_list_simple.data.dataSources

import com.gabez.todo_list_simple.data.firestore.responses.ApiResponse
import com.gabez.todo_list_simple.data.firestore.RemoteDatabase
import com.gabez.todo_list_simple.domain.entities.ItemTODO
import kotlinx.coroutines.flow.Flow

class RemoteDatasourceImpl(private val remoteDb: RemoteDatabase): RemoteDatasource {
    override fun addItem(item: ItemTODO): Flow<ApiResponse> = remoteDb.addItem(item)

    override suspend fun deleteItem(item: ItemTODO): Flow<ApiResponse> = remoteDb.deleteItem(item)

    override suspend fun editItem(item: ItemTODO): Flow<ApiResponse> = remoteDb.editItem(item)
}