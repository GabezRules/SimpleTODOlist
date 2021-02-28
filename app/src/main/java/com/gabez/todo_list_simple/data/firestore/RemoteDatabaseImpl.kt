package com.gabez.todo_list_simple.data.firestore

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.gabez.todo_list_simple.data.firestore.responses.ApiResponse
import com.gabez.todo_list_simple.data.firestore.responses.ApiResponseStatus
import com.gabez.todo_list_simple.domain.entities.ItemTODO
import com.gabez.todo_list_simple.domain.entities.toItemObject
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import com.gabez.todo_list_simple.domain.entities.toHashMap as toHashMap

@ExperimentalCoroutinesApi
class RemoteDatabaseImpl(val context: Context) : RemoteDatabase, RemoteDatabaseItemsProvider {
    val db = FirebaseFirestore.getInstance()

    init {
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()

        db.firestoreSettings = settings
    }

    override fun addItem(item: ItemTODO): Flow<ApiResponse> = setItem(item)

    override fun editItem(item: ItemTODO): Flow<ApiResponse> = setItem(item)

    override suspend fun deleteItem(item: ItemTODO): Flow<ApiResponse> = channelFlow {
        db.collection("items").document(item.ID)
            .delete()
            .addOnSuccessListener {
                launch {
                    send(ApiResponse(ApiResponseStatus.SUCCESS, "completed"))
                    close()
                }
            }
            .addOnFailureListener { e ->
                launch {
                    send(ApiResponse(ApiResponseStatus.FAILED, e.message))
                    close()
                }
            }

        awaitClose()
    }

    private fun setItem(item: ItemTODO): Flow<ApiResponse> = channelFlow {
        val itemMap = item.toHashMap()

        db.collection("items").document(item.ID)
            .set(itemMap)
            .addOnSuccessListener {
                launch {
                    send(ApiResponse(ApiResponseStatus.SUCCESS, "completed"))
                    close()
                }
            }
            .addOnFailureListener { e ->
                launch {
                    send(ApiResponse(ApiResponseStatus.FAILED, e.message))
                    close()
                }
            }

        awaitClose()

    }

    override fun getLast(index: Long): Flow<ApiResponse> = channelFlow {
        val limit = if(index > 0) index else 30

        db.collection("items")
            .limit(limit)
            .get()
            .addOnCompleteListener { data ->
                val itemsData = data.result!!.documents.map { docSnap -> docSnap.data!!.toItemObject() }

                launch {
                    Log.v("DB", "load remote data - send data...")
                    send(ApiResponse(ApiResponseStatus.SUCCESS, itemsData))
                    close()
                }
            }
            .addOnFailureListener { e ->
                launch {
                    send(ApiResponse(ApiResponseStatus.FAILED, e.message))
                    close()
                }
            }

        awaitClose()
    }
}

