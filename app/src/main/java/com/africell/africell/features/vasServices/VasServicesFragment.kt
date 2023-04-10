package com.africell.africell.features.vasServices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.africell.africell.R
import com.africell.africell.app.viewbinding.BaseVBFragment
import com.africell.africell.app.viewbinding.withVBAvailable
import com.africell.africell.data.api.ApiContract
import com.africell.africell.data.api.dto.ServicesDTO
import com.africell.africell.databinding.FragmentServicesBinding
import com.africell.africell.databinding.ToolbarServiceBinding
import com.africell.africell.features.services.ServiceDetailsFragment.Companion.SERVICE_DETAILS
import com.africell.africell.features.services.ServicesAdapter
import com.africell.africell.ui.viewmodel.observeResourceInline
import com.africell.africell.ui.viewmodel.provideViewModel


class VasServicesFragment : BaseVBFragment<FragmentServicesBinding>() {


    private val viewModel by provideViewModel<VasServicesViewModel> { viewModelFactory }

    val adapter by lazy {
        ServicesAdapter(mutableListOf(), object : ServicesAdapter.Callback {
            override fun onItemClickListener(item: ServicesDTO) {
                val bundle = bundleOf(Pair(SERVICE_DETAILS, item))
                findNavController().navigate(R.id.serviceDetailsFragment, bundle)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(container, FragmentServicesBinding::inflate, true, ToolbarServiceBinding::inflate)
    }


    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.title = ""
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_back)
        actionbar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getToolbarBindingAs<ToolbarServiceBinding>()?.run {
            setupImageBanner(toolbarImage, ApiContract.Params.BANNERS, ApiContract.ImagePageName.VAS_SERVICES)
        }
        setupRecyclerView()
        viewModel.getServices()
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
        observeResourceInline(viewModel.serviceData) {
            if (it.isNullOrEmpty()) {
                showInlineMessage(getString(R.string.no_services_available))
            } else {
                adapter.setItems(it)

            }
        }


    }
}
