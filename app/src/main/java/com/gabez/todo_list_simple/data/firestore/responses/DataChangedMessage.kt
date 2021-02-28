package com.gabez.todo_list_simple.data.firestore.responses

import com.gabez.todo_list_simple.domain.entities.ItemTODO
import com.google.firebase.firestore.DocumentChange

data class DataChangedMessage(val status: DocumentChange.Type, val data: ItemTODO)