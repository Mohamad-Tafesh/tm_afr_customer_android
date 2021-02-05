package com.tedmob.africell.features.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.drawee.view.SimpleDraweeView
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.data.api.ApiContract.ImagePageName.SERVICES
import com.tedmob.africell.data.api.ApiContract.Params.BANNERS
import com.tedmob.africell.data.api.dto.ServicesDTO
import com.tedmob.africell.features.home.HomeViewModel
import com.tedmob.africell.features.home.ImageViewModel
import com.tedmob.africell.features.services.ServiceDetailsFragment.Companion.SERVICE_DETAILS
import com.tedmob.africell.ui.viewmodel.*
import kotlinx.android.synthetic.main.fragment_services.*
import kotlinx.android.synthetic.main.toolbar_service.*
import javax.inject.Inject


class ServicesFragment : BaseFragment() {


    private val viewModel by provideViewModel<ServicesViewModel> { viewModelFactory }

    val adapter by lazy {
        ServicesAdapter(mutableListOf(), object : ServicesAdapter.Callback {
            override fun onItemClickListener(item: ServicesDTO) {
                val bundle = bundleOf(Pair(SERVICE_DETAILS, item))
                findNavController().navigate(R.id.action_servicesFragment_to_serviceDetailsFragment, bundle)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_services, R.layout.toolbar_service, true)
    }


    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.title = ""
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_side_menu)
        actionbar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupImageBanner(toolbarImage, BANNERS, SERVICES)
        setupRecyclerView()
        viewModel.getServices()
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
        observeResourceInline(viewModel.serviceData) {
            if (it.isNullOrEmpty()) {
                showInlineMessage(getString(R.string.no_services_available))
            } else {
                adapter.setItems(it)

            }
        }
    }

}
