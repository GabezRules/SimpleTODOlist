package com.gabez.todo_list_simple.app.itemValidation

data class ItemValidatorResponse(var isTitleValid: Boolean = false, var isDescriptionValid: Boolean = false, var isUrlValid: Boolean = false, var isValid: Boolean = false)