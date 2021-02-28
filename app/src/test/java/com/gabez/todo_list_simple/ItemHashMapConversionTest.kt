package com.gabez.todo_list_simple

import com.gabez.todo_list_simple.domain.entities.ItemTODO
import com.gabez.todo_list_simple.domain.entities.toHashMap
import com.gabez.todo_list_simple.domain.entities.toItemObject
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ItemHashMapConversionTest {

    val item = ItemTODO(
        title = "Test title",
        description = "Test description",
        iconUrl = "",
        creationDate = "11/11/2011 11:11"
    )

    val hashMap = HashMap<String, Any>()

    @Before
    fun setupTestObjects() {
        hashMap["title"] = "Test title"
        hashMap["description"] = "Test description"
        hashMap["iconUrl"] = ""
        hashMap["creationDate"] = "11/11/2011 11:11"
        hashMap["ID"] = item.ID
    }

    @Test
    fun itemToHashMapTest() {
        val hashMapFromItem = item.toHashMap()
        assertEquals(hashMapFromItem, hashMap)
    }

    @Test
    fun hashMapToItemTest() {
        val itemFromHashMap = hashMap.toItemObject()
        assertEquals(itemFromHashMap, item)
    }
}