package com.tedmob.africell.features.myBundlesAndServices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.data.api.dto.ServicesDTO
import com.tedmob.africell.features.bundles.BundleDetailsFragment.Companion.BUNDLE_DETAILS
import com.tedmob.africell.features.services.ServiceDetailsFragment.Companion.SERVICE_DETAILS
import com.tedmob.africell.features.services.ServicesAdapter
import com.tedmob.africell.ui.viewmodel.ViewModelFactory
import com.tedmob.africell.ui.viewmodel.provideViewModel
import kotlinx.android.synthetic.main.fragment_services.*
import javax.inject.Inject


class MyBundlesAndServicesFragment : BaseFragment() {


    private val viewModel by provideViewModel<MyBundlesAndServicesViewModel> { viewModelFactory }
    val bundle get() = arguments?.getParcelableArrayList<ServicesDTO>(KEY_BUNDLE)

    companion object {
        const val KEY_BUNDLE = "key_bundle"
        fun newInstance(bundle: List<ServicesDTO>?): MyBundlesAndServicesFragment {
            return MyBundlesAndServicesFragment().apply {
                arguments = bundleOf(KEY_BUNDLE to bundle)

            }
        }
    }

    val adapter by lazy {
        ServicesAdapter(mutableListOf(), object : ServicesAdapter.Callback {
            override fun onItemClickListener(item: ServicesDTO) {
                if (item.isServices == true) {
                     val bundle = bundleOf(Pair(SERVICE_DETAILS, item))
                         findNavController().navigate(R.id.serviceDetailsFragment, bundle)
                } else {
                    val bundle = bundleOf(Pair(BUNDLE_DETAILS, item))
                    findNavController().navigate(
                        R.id.action_myBundleServicesVPFragment_to_myBundleDetailsFragment,
                        bundle
                    )
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_services, 0, false)
    }


    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.title = ""
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

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
        if (bundle.isNullOrEmpty()) {
            showInlineMessage(getString(R.string.no_services_available))
        } else {
            adapter.setItems(bundle.orEmpty())

        }
    }

}
