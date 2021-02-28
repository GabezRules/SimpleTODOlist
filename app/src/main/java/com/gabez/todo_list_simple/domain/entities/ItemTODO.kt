package com.gabez.todo_list_simple.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.HashMap

@Parcelize
data class ItemTODO(
    var title: String = "",
    var description: String = "",
    var iconUrl: String = "",
    var creationDate: String  = ItemTODOHelper.getCurrentDate(),
    val ID: String = ItemTODOHelper.getItemId()): Parcelable

fun MutableMap<String, Any>.toItemObject(): ItemTODO {

    return ItemTODO(
        title = this["title"].toString(),
        description = this["description"].toString(),
        iconUrl = this["iconUrl"].toString(),
        creationDate = this["creationDate"].toString(),
        ID = this["ID"].toString()
    )
}

fun ItemTODO.toHashMap(): HashMap<String, String> {
    val map = HashMap<String, String>()
    map["title"] = this.title
    map["description"] = this.description
    map["iconUrl"] = this.iconUrl
    map["creationDate"] = this.creationDate
    map["ID"] = this.ID

    return map
}
