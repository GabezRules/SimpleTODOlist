package com.gabez.todo_list_simple.app.fragments.addItemView

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabez.todo_list_simple.app.itemValidation.ItemValidator
import com.gabez.todo_list_simple.app.itemValidation.ItemValidatorResponse
import com.gabez.todo_list_simple.data.firestore.responses.ApiResponseStatus
import com.gabez.todo_list_simple.domain.entities.ItemTODO
import com.gabez.todo_list_simple.domain.usecases.AddItemUsecase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AddItemViewModel(private val addItemUsecase: AddItemUsecase, private val context: Context) :
    ViewModel() {
    private val validator: ItemValidator = ItemValidator()
    val error: MutableLiveData<ItemValidatorResponse> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    fun addItem(item: ItemTODO): Boolean {
        val validatorResponse = validator.checkIfItemValid(item)
        isLoading.postValue(true)

        if (validatorResponse.isValid) {
            error.postValue(null)
            viewModelScope.launch {
                addItemUsecase.invoke(item).collect { response ->
                    when (response.status) {
                        ApiResponseStatus.SUCCESS -> {
                        }
                        ApiResponseStatus.FAILED -> response.data?.let {
                            showToast(it.toString())
                        }
                    }

                    isLoading.postValue(false)

                }
            }

        } else {
            isLoading.postValue(false)
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