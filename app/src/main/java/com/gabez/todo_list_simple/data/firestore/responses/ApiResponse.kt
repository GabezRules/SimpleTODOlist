package com.gabez.todo_list_simple.data.firestore.responses

import com.gabez.todo_list_simple.domain.entities.ItemTODO
import com.gabez.todo_list_simple.data.pagination.PageInfo
import com.google.gson.annotations.SerializedName

open class ApiResponse(open val status: ApiResponseStatus, open val data: Any?)

data class PagedResponse(
    @SerializedName("info") val pageInfo: PageInfo,
    override val data: List<ItemTODO> = listOf(),
    override val status: ApiResponseStatus ): ApiResponse(status, data)