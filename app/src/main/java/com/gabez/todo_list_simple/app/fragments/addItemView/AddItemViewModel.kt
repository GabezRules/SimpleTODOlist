package com.gabez.todo_list_simple.app.fragments.addItemView

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabez.todo_list_simple.ItemPreviewViewModel
import com.gabez.todo_list_simple.app.itemValidation.ItemValidator
import com.gabez.todo_list_simple.app.itemValidation.ItemValidatorResponse
import com.gabez.todo_list_simple.data.firestore.responses.ApiResponseStatus
import com.gabez.todo_list_simple.domain.entities.ItemTODO
import com.gabez.todo_list_simple.domain.usecases.ActionOnItemUsecase
import com.gabez.todo_list_simple.domain.usecases.AddItemUsecase
import com.gabez.todo_list_simple.domain.usecases.EditItemUsecase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AddItemViewModel(override val action: AddItemUsecase, override val context: Context) : ItemPreviewViewModel(action as ActionOnItemUsecase, context)