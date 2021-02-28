package com.gabez.todo_list_simple.domain.usecases

import com.gabez.todo_list_simple.data.firestore.responses.ApiResponse
import com.gabez.todo_list_simple.domain.entities.ItemTODO
import kotlinx.coroutines.flow.Flow

interface ActionOnItemUsecase {
    suspend operator fun invoke(item: ItemTODO): Flow<ApiResponse>
}