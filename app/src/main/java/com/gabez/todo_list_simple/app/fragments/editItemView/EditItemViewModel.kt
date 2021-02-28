package com.gabez.todo_list_simple.app.fragments.editItemView

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabez.todo_list_simple.app.itemValidation.ItemValidator
import com.gabez.todo_list_simple.app.itemValidation.ItemValidatorResponse
import com.gabez.todo_list_simple.data.firestore.responses.ApiResponseStatus
import com.gabez.todo_list_simple.domain.entities.ItemTODO
import com.gabez.todo_list_simple.domain.usecases.EditItemUsecase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class EditItemViewModel(
    private val editItemUsecase: EditItemUsecase,
    private val context: Context
) : ViewModel() {
    private val validator: ItemValidator = ItemValidator()
    val error: MutableLiveData<ItemValidatorResponse> = MutableLiveData()

    fun editItem(item: ItemTODO): Boolean {
        val validatorResponse = validator.checkIfItemValid(item)

        return if (validatorResponse.isValid) {
            error.postValue(null)
            viewModelScope.launch {
                editItemUsecase.invoke(item).collect { response ->
                    when (response.status) {
                        ApiResponseStatus.SUCCESS -> {
                        }
                        ApiResponseStatus.FAILED -> response.data?.let {
                            showToast(it.toString())
                        }
                    }
                }
            }
            true
        } else {
            error.postValue(validatorResponse)
            false
        }
    }

    private fun showToast(text: String) {
        GlobalScope.launch(Dispatchers.Main) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }
}