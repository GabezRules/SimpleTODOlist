package com.gabez.todo_list_simple.app.fragments.editItemView

import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.gabez.todo_list_simple.R
import com.gabez.todo_list_simple.app.ItemPreviewFragment
import org.koin.core.KoinComponent
import org.koin.core.inject

class EditItemFragment : ItemPreviewFragment(), KoinComponent {

    private val viewModel: EditItemViewModel by inject()
    override var fragmentDescriptionText: String? = "Edit item"

    override var buttonAction: View.OnClickListener? = View.OnClickListener {
        val isOk = viewModel.editItem(createTempItem())
        if(isOk) this@EditItemFragment.findNavController().popBackStack(R.id.editItemFragment, true)
    }

    override var backAction: View.OnClickListener? = View.OnClickListener {
        this@EditItemFragment.findNavController().popBackStack(R.id.editItemFragment, true)
    }

    override fun observeViewModelError() {
        viewModel.error.observe(viewLifecycleOwner, Observer {
            it?.let{setErrors(it)}
        })
    }
}