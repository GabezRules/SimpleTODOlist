package com.gabez.todo_list_simple.domain.repository

import com.gabez.todo_list_simple.data.firestore.responses.ApiResponse
import com.gabez.todo_list_simple.domain.entities.ItemTODO
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    fun addItem(item: ItemTODO): Flow<ApiResponse>
    suspend fun deleteItem(item: ItemTODO): Flow<ApiResponse>
    suspend fun editItem(item: ItemTODO): Flow<ApiResponse>
}