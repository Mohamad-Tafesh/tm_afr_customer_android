package com.tedmob.africell.features.usefulNumber

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.data.api.dto.ServicesDTO
import com.tedmob.africell.data.api.dto.UsefulNumberDTO
import com.tedmob.africell.ui.viewmodel.ViewModelFactory
import com.tedmob.africell.ui.viewmodel.observeResourceInline
import com.tedmob.africell.ui.viewmodel.provideViewModel
import com.tedmob.africell.util.intents.dial
import kotlinx.android.synthetic.main.fragment_services.*
import javax.inject.Inject


class UsefulNumberFragment : BaseFragment() {


    private val viewModel by provideViewModel<UsefulNumberViewModel> { viewModelFactory }

    val adapter by lazy {
        UsefulNumberAdapter(mutableListOf(), object : UsefulNumberAdapter.Callback {
            override fun onItemClickListener(item: UsefulNumberDTO) {
                item.number?.let {  dial(item.number)}
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_useful_number, R.layout.toolbar_default, true)
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
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.separator)
        drawable?.let {
            dividerItemDecoration.setDrawable(it)
        }
        recyclerView.addItemDecoration(dividerItemDecoration)
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
