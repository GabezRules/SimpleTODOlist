package com.gabez.todo_list_simple.dataChangesTest

import com.gabez.todo_list_simple.data.firestore.responses.DataChangedMessage
import com.gabez.todo_list_simple.domain.entities.ItemTODO
import com.google.firebase.firestore.DocumentChange
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DataChangesTest {

    private val viewModelMock = ListViewModelMock()
    private val mockData = ArrayList<ItemTODO>()

    private val addItemData = ArrayList<DataChangedMessage>()
    private val updateItemData = ArrayList<DataChangedMessage>()
    private val deleteItemData = ArrayList<DataChangedMessage>()

    @Before
    fun setupData(){
        mockData.add(
            ItemTODO(
                title = "Sample title",
                description = "Sample description",
                creationDate = "11/11/2011 11:11",
                ID = "111"
            )
        )

        mockData.add(
            ItemTODO(
                title = "Title sample",
                description = "Description sample",
                creationDate = "22/12/2022 22:22",
                ID = "222"
            )
        )

        mockData.add(
            ItemTODO(
                title = "Title text",
                description = "Description text",
                creationDate = "13/03/2033 03:33",
                ID = "333"
            )
        )

        addItemData.add(
            DataChangedMessage(
                status = DocumentChange.Type.ADDED,
                data = ItemTODO(
                    title = "Another title",
                    description = "Another description",
                    creationDate = "14/04/2044 14:44",
                    ID = "444"
                )
            )
        )

        updateItemData.add(
            DataChangedMessage(
                status = DocumentChange.Type.MODIFIED,
                data = ItemTODO(
                    title = "Sample title",
                    description = "Sample description",
                    creationDate = "15/05/2055 15:55",
                    ID = "111"
                )
            )
        )

        deleteItemData.add(
            DataChangedMessage(
                status = DocumentChange.Type.REMOVED,
                data = ItemTODO(
                    title = "Title sample",
                    description = "Description sample",
                    creationDate = "22/12/2022 22:22",
                    ID = "222"
                )
            )
        )
    }

    @Test
    fun dataUpdated_addItem(){
        viewModelMock.data = mockData.clone() as ArrayList<ItemTODO>
        viewModelMock.updateItemsPostChange(addItemData)

        val mockExpectedData = mockData.clone() as ArrayList<ItemTODO>
        mockExpectedData.add(addItemData[0].data)

        assertEquals(viewModelMock.data, mockExpectedData)
    }

    @Test
    fun dataUpdated_updateItem(){
        viewModelMock.data = mockData.clone() as ArrayList<ItemTODO>
        viewModelMock.updateItemsPostChange(updateItemData)

        val mockExpectedData = mockData.clone() as ArrayList<ItemTODO>
        mockExpectedData[0] = updateItemData[0].data

        assertEquals(viewModelMock.data, mockExpectedData)
    }

    @Test
    fun dataUpdated_deleteItem(){
        viewModelMock.data = mockData.clone() as ArrayList<ItemTODO>
        viewModelMock.updateItemsPostChange(deleteItemData)

        val mockExpectedData = mockData.clone() as ArrayList<ItemTODO>
        mockExpectedData.remove(deleteItemData[0].data)

        assertEquals(viewModelMock.data, mockExpectedData)
    }
}