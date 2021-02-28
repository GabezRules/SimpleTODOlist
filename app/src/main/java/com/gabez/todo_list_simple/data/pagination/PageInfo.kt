package com.gabez.todo_list_simple.data.pagination

data class PageInfo(
    val count: Int,
    val pages: Int,
    val next: String,
    val prev: String?
)