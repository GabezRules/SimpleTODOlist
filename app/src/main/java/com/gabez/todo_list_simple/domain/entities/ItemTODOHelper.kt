package com.gabez.todo_list_simple.domain.entities

import java.text.SimpleDateFormat
import java.util.*

class ItemTODOHelper {
    companion object{
        val TITLE_LENGTH: Int = 30
        val DESCRIPTION_LENGTH: Int = 200

        val PLACEHOLDER_URL: String = "https://www.clipartkey.com/mpngs/m/128-1288930_cute-cat-emote-png-clipart-png-download-cute.png"

        fun getCurrentDate(): String{
            val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm")
            return sdf.format(Date())
        }

        fun getItemId(): String{
            val id = System.currentTimeMillis().toString().plus((0..1000).random().toString())
            return id
        }
    }

}