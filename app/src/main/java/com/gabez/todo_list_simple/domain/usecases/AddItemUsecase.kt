package com.gabez.todo_list_simple.domain.usecases

import com.gabez.todo_list_simple.data.firestore.responses.ApiResponse
import com.gabez.todo_list_simple.domain.entities.ItemTODO
import com.gabez.todo_list_simple.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow

class AddItemUsecase(private val repo: AppRepository) {
    operator fun invoke(item: ItemTODO): Flow<ApiResponse> = repo.addItem(item)
}