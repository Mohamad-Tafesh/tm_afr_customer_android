package com.tedmob.afrimoney.features.faq

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.databinding.FragmentFaqBinding
import com.tedmob.afrimoney.ui.viewmodel.observeResourceInline
import com.tedmob.afrimoney.ui.viewmodel.provideViewModel
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
        return createViewBinding(container, FragmentFaqBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupList()
        bindItems()
     withVBAvailable {
         recyclerView.addItemDecoration(
             MaterialDividerItemDecoration(
                 recyclerView.context,
                 MaterialDividerItemDecoration.VERTICAL
             )
                 .apply {
                     setDividerColorResource(recyclerView.context, R.color.greyHighlight)
                     setDividerInsetStartResource(recyclerView.context, R.dimen.divider_inset_start)
                 }
         )
     }

        viewModel.getItems()
    }

    private fun setupList() {
        binding?.recyclerView?.adapter = adapter
    }

    private fun bindItems() {
        observeResourceInline(viewModel.items) {
            adapter.submitList(it)
        }
    }
}
