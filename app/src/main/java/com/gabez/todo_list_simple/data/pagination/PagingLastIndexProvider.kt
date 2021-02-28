package com.gabez.todo_list_simple.data.pagination

import androidx.lifecycle.LiveData

interface PagingLastIndexProvider {
    val lastIndex: LiveData<Int>
}