package com.gabez.todo_list_simple.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.gabez.todo_list_simple.domain.entities.ItemTODOHelper
import com.gabez.todo_list_simple.R
import com.gabez.todo_list_simple.app.itemValidation.ItemValidatorResponse
import com.gabez.todo_list_simple.app.viewComponents.GlideImageComponent
import com.gabez.todo_list_simple.domain.entities.ItemTODO
import com.google.android.material.textfield.TextInputEditText

abstract class ItemPreviewFragment : Fragment() {

    private lateinit var fragmentDescription: TextView
    private lateinit var iconPreview: ImageView

    private lateinit var urlInputBody: TextInputEditText
    private lateinit var titleInputBody: TextInputEditText
    private lateinit var descriptionInputBody: TextInputEditText

    private lateinit var buttonAccept: View

    private lateinit var backArrow: ImageView
    private lateinit var progressBar: ProgressBar

    private lateinit var imageComponent: GlideImageComponent

    open var fragmentDescriptionText: String? = null
    open var buttonAction: View.OnClickListener? = null
    open var backAction: View.OnClickListener? = null

    private var currentItem: ItemTODO? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_preview, container, false)
        initViews(view)

        fragmentDescription.text = fragmentDescriptionText!!
        buttonAccept.setOnClickListener(buttonAction!!)
        backArrow.setOnClickListener(backAction!!)

        getBaseItemTODO()?.let {
            currentItem = it
            urlInputBody.setText(it.iconUrl)
            titleInputBody.setText(it.title)
            descriptionInputBody.setText(it.description)
        }

        imageComponent.setUrl(currentItem?.iconUrl)

        setupImageDynamicLoading()
        observeViewModelError()

        return view
    }

    fun setErrors(errorHolder: ItemValidatorResponse) {
        if (!errorHolder.isUrlValid) urlInputBody.error = "enter a valid URL"
        if (!errorHolder.isDescriptionValid) descriptionInputBody.error =
            "enter a valid description"
        if (!errorHolder.isTitleValid) titleInputBody.error = "enter a valid title"
    }

    open fun observeViewModelError() { /*no op*/ }

    private fun initViews(view: View) {
        fragmentDescription = view.findViewById(R.id.fragmentDescription)

        urlInputBody = view.findViewById(R.id.urlInputBody)
        titleInputBody = view.findViewById(R.id.titleInputBody)
        descriptionInputBody = view.findViewById(R.id.descriptionInputBody)

        buttonAccept = view.findViewById(R.id.buttonAccept)

        backArrow = view.findViewById(R.id.backArrow)

        iconPreview = view.findViewById(R.id.iconPreview)
        progressBar = view.findViewById(R.id.progressBar)
        imageComponent = GlideImageComponent(iconPreview, progressBar, requireContext())
    }

    fun createTempItem(): ItemTODO {
        return ItemTODO(
            title = titleInputBody.text.toString(),
            description = descriptionInputBody.text.toString(),
            iconUrl = urlInputBody.text.toString(),
            creationDate = currentItem?.creationDate ?: ItemTODOHelper.getCurrentDate(),
            ID = currentItem?.ID ?: ItemTODOHelper.getItemId()
        )
    }

    private fun getBaseItemTODO(): ItemTODO? {
        (arguments?.get("baseItem"))?.let {
            return (it as ItemTODO)
        } ?: return null
    }

    private fun setupImageDynamicLoading(){
        urlInputBody.doOnTextChanged { text, _, _, _ ->
            imageComponent.setUrl(text.toString())
        }
    }

}
