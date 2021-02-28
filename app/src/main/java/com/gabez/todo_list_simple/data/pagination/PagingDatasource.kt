package com.gabez.todo_list_simple.data.pagination

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gabez.todo_list_simple.domain.entities.ItemTODO
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class PagingDatasource : PagingSource<QuerySnapshot, ItemTODO>(), PagingLastIndexProvider {

    private val db = FirebaseFirestore.getInstance()
    private val _lastIndex: MutableLiveData<Int> = MutableLiveData(0)
    override val lastIndex: LiveData<Int> = _lastIndex

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, ItemTODO> {
        return try {
            val currentPage = params.key ?: db.collection("items")
                .limit(30)
                .get()
                .await()

            val lastDocumentSnapshot = currentPage.documents[currentPage.size() - 1]

            val nextPage = db.collection("items").limit(30).startAfter(lastDocumentSnapshot)
                .get()
                .await()

            if(nextPage.size() - 1 <= 0) _lastIndex.postValue(30)
            else _lastIndex.postValue(nextPage.size() - 1)

            LoadResult.Page(
                data = currentPage.toObjects(ItemTODO::class.java),
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<QuerySnapshot, ItemTODO>): QuerySnapshot? {
        return null
    }
}

