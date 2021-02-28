package com.gabez.todo_list_simple.app.fragments.addItemView

import android.view.View
import androidx.navigation.fragment.findNavController
import com.gabez.todo_list_simple.R
import com.gabez.todo_list_simple.app.ItemPreviewFragment
import org.koin.core.KoinComponent
import org.koin.core.inject

class AddItemFragment : ItemPreviewFragment(), KoinComponent {

    private val viewModel: AddItemViewModel by inject()
    override var fragmentDescriptionText: String? = "Add item"
    override var buttonAction: View.OnClickListener? = View.OnClickListener {
        val isOk = viewModel.addItem(createTempItem())
        if(isOk) this@AddItemFragment.findNavController().popBackStack(R.id.addItemFragment, true)
    }

    override var backAction: View.OnClickListener? = View.OnClickListener {
        this@AddItemFragment.findNavController().popBackStack(R.id.addItemFragment, true)
    }

    override fun observeViewModelError() {
        viewModel.error.observe(viewLifecycleOwner, {
            it?.let{setErrors(it)}
        })
    }
}