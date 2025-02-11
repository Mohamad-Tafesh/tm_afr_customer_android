package com.africell.africell.features.usefulNumber

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
import com.africell.africell.data.api.dto.UsefulNumberDTO
import com.africell.africell.databinding.FragmentUsefulNumberBinding
import com.africell.africell.databinding.ToolbarDefaultBinding
import com.africell.africell.ui.viewmodel.observeResourceInline
import com.africell.africell.ui.viewmodel.provideViewModel
import com.africell.africell.util.intents.dial
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UsefulNumberFragment : BaseVBFragment<FragmentUsefulNumberBinding>() {


    private val viewModel by provideViewModel<UsefulNumberViewModel>()

    val adapter by lazy {
        UsefulNumberAdapter(mutableListOf(), object : UsefulNumberAdapter.Callback {
            override fun onItemClickListener(item: UsefulNumberDTO) {
                item.number?.let { dial(item.number) }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(container, FragmentUsefulNumberBinding::inflate, true, ToolbarDefaultBinding::inflate)
    }


    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.title = getString(R.string.useful_number)
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_side_menu)
        actionbar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewModel.getUsefulNumber()
        bindData()
    }


    private fun setupRecyclerView() {
        withVBAvailable {
            recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
            recyclerView.adapter = adapter
            val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.separator)
            drawable?.let {
                dividerItemDecoration.setDrawable(it)
            }
            recyclerView.addItemDecoration(dividerItemDecoration)
        }
    }

    private fun bindData() {
        observeResourceInline(viewModel.usefulData) {
            if (it.isNullOrEmpty()) {
                showInlineMessage(getString(R.string.no_services_available))
            } else {
                adapter.setItems(it)
            }
        }


    }
}
