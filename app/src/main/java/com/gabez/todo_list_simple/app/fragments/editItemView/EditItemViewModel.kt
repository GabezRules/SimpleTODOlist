package com.gabez.todo_list_simple.app.fragments.editItemView

import android.content.Context

import com.gabez.todo_list_simple.ItemPreviewViewModel
import com.gabez.todo_list_simple.domain.usecases.ActionOnItemUsecase
import com.gabez.todo_list_simple.domain.usecases.EditItemUsecase

class EditItemViewModel(override val action: EditItemUsecase, override val context: Context) : ItemPreviewViewModel(action as ActionOnItemUsecase, context)