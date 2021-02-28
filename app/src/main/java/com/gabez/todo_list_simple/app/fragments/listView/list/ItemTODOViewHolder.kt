package com.gabez.todo_list_simple.app.fragments.listView.list

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gabez.todo_list_simple.R
import com.gabez.todo_list_simple.app.viewComponents.GlideImageComponent
import com.gabez.todo_list_simple.domain.entities.ItemTODO

class ItemTODOViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val singleItemIcon: ImageView = itemView.findViewById(R.id.singleItemIcon)
    private val singleItemTitle: TextView = itemView.findViewById(R.id.singleItemTitle)
    private val singleItemCreationDate: TextView = itemView.findViewById(R.id.singleItemCreationDate)
    private val singleItemDescription: TextView = itemView.findViewById(R.id.singleItemDescription)

    val singleItemContainer: View = itemView.findViewById(R.id.singleItemContainer)

    private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)

    fun bindView(itemTODO: ItemTODO) {
        singleItemTitle.text = itemTODO.title
        singleItemDescription.text = itemTODO.description
        singleItemCreationDate.text = "Created: ${itemTODO.creationDate}"

        var imagecomponent = GlideImageComponent(singleItemIcon, progressBar, itemView.context)
        imagecomponent.setUrl(itemTODO.iconUrl)
    }
}