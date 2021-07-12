package com.africell.africell.features.terms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.africell.africell.app.BaseFragment
import com.africell.africell.R
import com.africell.africell.ui.viewmodel.observeResourceInline
import com.africell.africell.ui.viewmodel.provideViewModel
import com.africell.africell.util.html.html
import kotlinx.android.synthetic.main.fragment_terms.*

class TermsFragment : BaseFragment() {

    private val viewModel by provideViewModel<TermsViewModel> { viewModelFactory}
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return wrap(inflater.context, R.layout.fragment_terms, R.layout.toolbar_default, true)
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
            imageView.setImageURI(terms.image)
            descriptionTxt.text = terms.description.orEmpty().html()
        }
    }
}
