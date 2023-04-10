package com.africell.africell.features.afrimoneyBundles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.africell.africell.R
import com.africell.africell.app.viewbinding.BaseVBFragment
import com.africell.africell.app.viewbinding.withVBAvailable
import com.africell.africell.data.api.dto.BundleInfo
import com.africell.africell.data.api.dto.BundlesDTO
import com.africell.africell.databinding.FragmentBundlesBinding
import com.africell.africell.features.bundles.BundleDetailsFragment.Companion.BUNDLE_ID
import com.africell.africell.ui.viewmodel.provideViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AfrimoneyBundlesFragment : BaseVBFragment<FragmentBundlesBinding>() {

    val bundle get() = arguments?.getParcelable<BundlesDTO>(KEY_BUNDLE)

    companion object {
        const val KEY_BUNDLE = "key_bundle"
        fun newInstance(bundle: BundlesDTO?): AfrimoneyBundlesFragment {
            return AfrimoneyBundlesFragment().apply {
                arguments = bundleOf(KEY_BUNDLE to bundle)

            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(container, FragmentBundlesBinding::inflate, false)
    }


    private val viewModel by provideViewModel<AfrimoneyBundlesViewModel>()

    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)

    }


    val adapter by lazy {
        AfrimoneyBundleAdapter(mutableListOf(), object : AfrimoneyBundleAdapter.Callback {
            override fun onItemClickListener(item: BundleInfo) {
                val bundle = bundleOf(
                    BUNDLE_ID to item.bundleId?.toString(),
                    AfrimoneyBundleDetailsFragment.KEY_PRIMARY_COLOR_HEX to item.primaryColor
                )
                findNavController().navigate(
                    R.id.action_afrimoneyBundleVPFragment_to_afrimoneyBundleDetailsFragment,
                    bundle
                )
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupCategoriesItem()

    }


    private fun setupCategoriesItem() {
        withVBAvailable {
            recyclerView.layoutManager = GridLayoutManager(context, 3)
            recyclerView.adapter = adapter
            recyclerView.isNestedScrollingEnabled = true
            adapter.setItems(bundle?.bundleInfo.orEmpty().toMutableList())
        }
    }


}