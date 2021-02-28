package com.gabez.todo_list_simple.app.itemValidation

import com.gabez.todo_list_simple.domain.entities.ItemTODOHelper
import com.gabez.todo_list_simple.domain.entities.ItemTODO
import java.net.URL
import java.net.URLConnection

class ItemValidator {
    fun checkIfItemValid(item: ItemTODO): ItemValidatorResponse {
        var response = ItemValidatorResponse(
            isTitleValid = isTitleValid(item.title),
            isDescriptionValid = isDescriptionValid(item.description),
            isUrlValid = isUrlValid(item.iconUrl)
        )

        if (response.isTitleValid && response.isDescriptionValid && response.isUrlValid) response.isValid =
            true

        return response
    }

    private fun isTitleValid(title: String) =
        title.replace("\\s".toRegex(), "") != "" && title.length <= ItemTODOHelper.TITLE_LENGTH

    private fun isDescriptionValid(description: String) =
        description.replace(
            "\\s".toRegex(),
            ""
        ) != "" && description.length < ItemTODOHelper.DESCRIPTION_LENGTH

    private fun isUrlValid(url: String): Boolean {
        return if(url.replace("\\s".toRegex(),"")=="") true
        else {
            val connection: URLConnection = URL(url).openConnection()
            val contentType: String = connection.getHeaderField("Content-Type")
            contentType.startsWith("image/")
        }
    }
}