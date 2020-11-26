package com.tedmob.africell.features.terms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.R
import com.tedmob.africell.ui.viewmodel.ViewModelFactory
import com.tedmob.africell.ui.viewmodel.observeResourceInline
import com.tedmob.africell.ui.viewmodel.provideViewModel
import com.tedmob.africell.util.html.html
import kotlinx.android.synthetic.main.fragment_terms.*
import javax.inject.Inject

class TermsFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
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
