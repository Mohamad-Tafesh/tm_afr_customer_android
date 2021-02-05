package com.tedmob.africell.features.faq

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.ui.viewmodel.ViewModelFactory
import com.tedmob.africell.ui.viewmodel.observeResourceInline
import com.tedmob.africell.ui.viewmodel.provideViewModel
import kotlinx.android.synthetic.main.fragment_faq.*
import javax.inject.Inject

class FaqFragment : BaseFragment() {


    private val viewModel by provideViewModel<FaqViewModel> { viewModelFactory }

    private val adapter: FaqAdapter by lazy { FaqAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return wrap(inflater.context, R.layout.fragment_faq, R.layout.toolbar_default, true)
    }

    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.title = getString(R.string.faq)
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_side_menu)
        actionbar?.setDisplayHomeAsUpEnabled(true)

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupList()

        bindItems()

        viewModel.getItems()
    }

    private fun setupList() {
        val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.separator)
        drawable?.let {
            dividerItemDecoration.setDrawable(it)
        }
        recyclerView.addItemDecoration(dividerItemDecoration)
        recyclerView.adapter = adapter
    }

    private fun bindItems() {
        observeResourceInline(viewModel.items) {
            adapter.submitList(it)
        }
    }
}
