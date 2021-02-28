package com.gabez.todo_list_simple

import com.gabez.todo_list_simple.app.itemValidation.ItemValidator
import com.gabez.todo_list_simple.domain.entities.ItemTODO
import com.gabez.todo_list_simple.domain.entities.ItemTODOHelper
import org.junit.Test

import org.junit.Assert.*

class ItemValidatorTest {
    val validator = ItemValidator()

    @Test
    fun testCase_validItem() {
        val testCase = ItemTODO(
            title = "Test title",
            description = "Test description",
            iconUrl = ItemTODOHelper.PLACEHOLDER_URL
        )

        val validatorResponse = validator.checkIfItemValid(testCase)

        assertEquals(true, validatorResponse.isTitleValid)
        assertEquals(true, validatorResponse.isDescriptionValid)
        assertEquals(true, validatorResponse.isUrlValid)
        assertEquals(true, validatorResponse.isValid)
    }

    @Test
    fun testCase_emptyTitle() {
        val testCase = ItemTODO(
            title = "",
            description = "Test description",
            iconUrl = ItemTODOHelper.PLACEHOLDER_URL
        )

        val validatorResponse = validator.checkIfItemValid(testCase)

        assertEquals(false, validatorResponse.isTitleValid)
        assertEquals(true, validatorResponse.isDescriptionValid)
        assertEquals(true, validatorResponse.isUrlValid)
        assertEquals(false, validatorResponse.isValid)
    }

    @Test
    fun testCase_emptyDescription() {

        val testCase = ItemTODO(
            title = "Test title",
            description = "",
            iconUrl = ItemTODOHelper.PLACEHOLDER_URL
        )

        val validatorResponse = validator.checkIfItemValid(testCase)

        assertEquals(true, validatorResponse.isTitleValid)
        assertEquals(false, validatorResponse.isDescriptionValid)
        assertEquals(true, validatorResponse.isUrlValid)
        assertEquals(false, validatorResponse.isValid)
    }

    @Test
    fun testCase_emptyUrl() {
        val testCase = ItemTODO(
            title = "Test title",
            description = "Test description",
            iconUrl = ""
        )

        val validatorResponse = validator.checkIfItemValid(testCase)

        assertEquals(true, validatorResponse.isTitleValid)
        assertEquals(true, validatorResponse.isDescriptionValid)
        assertEquals(true, validatorResponse.isUrlValid)
        assertEquals(true, validatorResponse.isValid)
    }

    @Test
    fun testCase_invalidUrl() {
        val validator = ItemValidator()
        val testCase = ItemTODO(
            title = "Test title",
            description = "Test description",
            iconUrl = "https://www.facebook.com/"
        )

        val validatorResponse = validator.checkIfItemValid(testCase)

        assertEquals(true, validatorResponse.isTitleValid)
        assertEquals(true, validatorResponse.isDescriptionValid)
        assertEquals(false, validatorResponse.isUrlValid)
        assertEquals(false, validatorResponse.isValid)
    }

    @Test
    fun testCase_invalidUrl2() {
        val validator = ItemValidator()
        val testCase = ItemTODO(
            title = "Test title",
            description = "Test description",
            iconUrl = "https://ww"
        )

        val validatorResponse = validator.checkIfItemValid(testCase)

        assertEquals(true, validatorResponse.isTitleValid)
        assertEquals(true, validatorResponse.isDescriptionValid)
        assertEquals(false, validatorResponse.isUrlValid)
        assertEquals(false, validatorResponse.isValid)
    }
}