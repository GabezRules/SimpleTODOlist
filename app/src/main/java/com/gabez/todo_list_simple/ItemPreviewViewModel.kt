package com.gabez.todo_list_simple

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabez.todo_list_simple.app.itemValidation.ItemValidator
import com.gabez.todo_list_simple.app.itemValidation.ItemValidatorResponse
import com.gabez.todo_list_simple.data.firestore.responses.ApiResponseStatus
import com.gabez.todo_list_simple.domain.entities.ItemTODO
import com.gabez.todo_list_simple.domain.usecases.ActionOnItemUsecase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class ItemPreviewViewModel(open val action: ActionOnItemUsecase, open val context: Context): ViewModel() {
    open val validator: ItemValidator = ItemValidator()
    open val error: MutableLiveData<ItemValidatorResponse> = MutableLiveData()

    fun invokeAction(item: ItemTODO): Boolean {
        val validatorResponse = validator.checkIfItemValid(item)

        if (validatorResponse.isValid) {
            error.postValue(null)
            viewModelScope.launch {
                action.invoke(item).collect { response ->
                    when (response.status) {
                        ApiResponseStatus.SUCCESS -> {
                        }
                        ApiResponseStatus.FAILED -> response.data?.let {
                            showToast(it.toString())
                        }
                    }

                }
            }

        } else {
            error.postValue(validatorResponse)
        }

        return validatorResponse.isValid
    }

    private fun showToast(text: String) {
        GlobalScope.launch(Dispatchers.Main) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }
}