package com.africell.africell.features.faq

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.africell.africell.R
import com.africell.africell.app.viewbinding.BaseVBFragment
import com.africell.africell.app.viewbinding.withVBAvailable
import com.africell.africell.databinding.FragmentFaqBinding
import com.africell.africell.databinding.ToolbarDefaultBinding
import com.africell.africell.ui.viewmodel.observeResourceInline
import com.africell.africell.ui.viewmodel.provideViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FaqFragment : BaseVBFragment<FragmentFaqBinding>() {


    private val viewModel by provideViewModel<FaqViewModel>()

    private val adapter: FaqAdapter by lazy { FaqAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentFaqBinding::inflate, true, ToolbarDefaultBinding::inflate)
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
        withVBAvailable {
            val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.separator)
            drawable?.let {
                dividerItemDecoration.setDrawable(it)
            }
            recyclerView.addItemDecoration(dividerItemDecoration)
            recyclerView.adapter = adapter
        }
    }

    private fun bindItems() {
        observeResourceInline(viewModel.items) {
            adapter.submitList(it)
        }
    }
}
