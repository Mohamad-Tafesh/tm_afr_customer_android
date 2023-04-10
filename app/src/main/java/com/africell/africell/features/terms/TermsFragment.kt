package com.africell.africell.features.terms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.africell.africell.R
import com.africell.africell.app.viewbinding.BaseVBFragment
import com.africell.africell.app.viewbinding.withVBAvailable
import com.africell.africell.databinding.FragmentTermsBinding
import com.africell.africell.databinding.ToolbarDefaultBinding
import com.africell.africell.ui.viewmodel.observeResourceInline
import com.africell.africell.ui.viewmodel.provideViewModel
import com.africell.africell.util.html.html
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TermsFragment : BaseVBFragment<FragmentTermsBinding>() {

    private val viewModel by provideViewModel<TermsViewModel>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return createViewBinding(container, FragmentTermsBinding::inflate, true, ToolbarDefaultBinding::inflate)
    }

    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_side_menu)
        actionbar?.title = ""
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindData()
        viewModel.getData()
    }

    private fun bindData() {
        observeResourceInline(viewModel.data) { terms ->
            withVBAvailable {
                imageView.setImageURI(terms.image)
                descriptionTxt.text = terms.description.orEmpty().html()
            }
        }
    }
}
