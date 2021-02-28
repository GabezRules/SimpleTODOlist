package com.gabez.todo_list_simple.data

import com.gabez.todo_list_simple.data.dataSources.RemoteDatasource
import com.gabez.todo_list_simple.data.firestore.responses.ApiResponse
import com.gabez.todo_list_simple.domain.entities.ItemTODO
import com.gabez.todo_list_simple.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow

class AppRepositoryImpl(private val remoteDatasource: RemoteDatasource) : AppRepository {

    override fun addItem(item: ItemTODO): Flow<ApiResponse> = remoteDatasource.addItem(item)

    override suspend fun deleteItem(item: ItemTODO): Flow<ApiResponse> = remoteDatasource.deleteItem(item)

    override suspend fun editItem(item: ItemTODO): Flow<ApiResponse> = remoteDatasource.editItem(item)

}