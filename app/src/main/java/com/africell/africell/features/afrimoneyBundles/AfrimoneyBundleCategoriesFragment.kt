package com.africell.africell.features.afrimoneyBundles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.africell.africell.R
import com.africell.africell.app.viewbinding.BaseVBFragment
import com.africell.africell.app.viewbinding.withVBAvailable
import com.africell.africell.data.api.dto.BundleCategoriesDTO
import com.africell.africell.databinding.FragmentBundleCategoriesBinding
import com.africell.africell.databinding.ToolbarDefaultBinding
import com.africell.africell.features.bundles.BundleVPFragment.Companion.KEY_BUNDLE_CATEGORY_ID
import com.africell.africell.features.bundles.BundleVPFragment.Companion.KEY_CATEGORY_NAME
import com.africell.africell.features.bundles.BundleVPFragment.Companion.KEY_PRIMARY_COLOR_HEX
import com.africell.africell.features.bundles.BundleVPFragment.Companion.KEY_SECONDARY_COLOR_HEX
import com.africell.africell.ui.viewmodel.observeResourceInline
import com.africell.africell.ui.viewmodel.provideViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AfrimoneyBundleCategoriesFragment : BaseVBFragment<FragmentBundleCategoriesBinding>() {

    private val viewModel by provideViewModel<AfrimoneyBundlesViewModel>()

    val adapter by lazy {
        AfrimoneyBundleCategoriesAdapter(mutableListOf(), object : AfrimoneyBundleCategoriesAdapter.Callback {
            override fun onItemClickListener(item: BundleCategoriesDTO) {
                val bundle = bundleOf(
                    Pair(KEY_BUNDLE_CATEGORY_ID, item.idBundleCategories?.toString()),
                    Pair(KEY_CATEGORY_NAME, item.categoryName),
                    Pair(KEY_PRIMARY_COLOR_HEX, item.primaryColor),
                    Pair(KEY_SECONDARY_COLOR_HEX, item.secondaryColor)
                )
                findNavController().navigate(
                    R.id.action_afrimoneyBundleCategoriesFragment_to_afrimoneyBundleVPFragment,
                    bundle
                )
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(
            container,
            FragmentBundleCategoriesBinding::inflate,
            true,
            ToolbarDefaultBinding::inflate
        )
    }

    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.title = getString(R.string.bundles)
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_back)
        actionbar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewModel.getBundlesCategories()
        bindData()
    }


    private fun setupRecyclerView() {
        withVBAvailable {
            recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
            recyclerView.adapter = adapter
        }
    }

    private fun bindData() {
        observeResourceInline(viewModel.bundleCategoriesData) {
            if (it.isNullOrEmpty()) {
                showInlineMessage(getString(R.string.no_bundle_available))
            }
            adapter.setItems(it)
        }
    }
}

