package com.gabez.todo_list_simple.data.firestore

import com.gabez.todo_list_simple.data.firestore.responses.ApiResponse
import kotlinx.coroutines.flow.Flow

interface RemoteDatabaseItemsProvider {
    fun getLast(index: Long): Flow<ApiResponse>
}