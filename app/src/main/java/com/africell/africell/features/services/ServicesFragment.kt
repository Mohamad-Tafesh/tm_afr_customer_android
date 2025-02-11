package com.africell.africell.features.services

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
import com.africell.africell.data.api.ApiContract.ImagePageName.SERVICES
import com.africell.africell.data.api.ApiContract.Params.BANNERS
import com.africell.africell.data.api.dto.ServicesDTO
import com.africell.africell.data.repository.domain.SessionRepository
import com.africell.africell.databinding.FragmentServicesBinding
import com.africell.africell.databinding.ToolbarServiceBinding
import com.africell.africell.features.services.ServiceDetailsFragment.Companion.SERVICE_DETAILS
import com.africell.africell.ui.viewmodel.observeResourceInline
import com.africell.africell.ui.viewmodel.provideViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ServicesFragment : BaseVBFragment<FragmentServicesBinding>() {


    private val viewModel by provideViewModel<ServicesViewModel>()

    @Inject
    lateinit var sessionRepository: SessionRepository

    val adapter by lazy {
        ServicesAdapter(mutableListOf(), object : ServicesAdapter.Callback {
            override fun onItemClickListener(item: ServicesDTO) {
                val bundle = bundleOf(Pair(SERVICE_DETAILS, item))
                findNavController().navigate(R.id.action_servicesFragment_to_serviceDetailsFragment, bundle)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(container, FragmentServicesBinding::inflate, true, ToolbarServiceBinding::inflate)
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
        if (sessionRepository.isLoggedIn()) {
            getToolbarBindingAs<ToolbarServiceBinding>()?.run {
                setupImageBanner(toolbarImage, BANNERS, SERVICES)
            }
            setupRecyclerView()
            viewModel.getServices()
            bindData()
        } else {
            showInlineMessageWithAction(getString(R.string.login_first), actionName = getString(R.string.login)) {
                redirectToLogin()
            }
        }

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
