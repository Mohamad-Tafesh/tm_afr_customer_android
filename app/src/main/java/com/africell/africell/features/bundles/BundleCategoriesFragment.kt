package com.africell.africell.features.bundles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.africell.africell.R
import com.africell.africell.app.BaseFragment
import com.africell.africell.data.api.dto.BundleCategoriesDTO
import com.africell.africell.features.bundles.BundleVPFragment.Companion.KEY_BUNDLE_CATEGORY_ID
import com.africell.africell.features.bundles.BundleVPFragment.Companion.KEY_CATEGORY_NAME
import com.africell.africell.features.bundles.BundleVPFragment.Companion.KEY_PRIMARY_COLOR_HEX
import com.africell.africell.features.bundles.BundleVPFragment.Companion.KEY_SECONDARY_COLOR_HEX
import com.africell.africell.ui.viewmodel.observeResourceInline
import com.africell.africell.ui.viewmodel.provideViewModel
import kotlinx.android.synthetic.main.fragment_bundle_categories.*


class BundleCategoriesFragment : BaseFragment() {

    private val viewModel by provideViewModel<BundlesViewModel> { viewModelFactory }

    val adapter by lazy {
        BundleCategoriesAdapter(mutableListOf(), object : BundleCategoriesAdapter.Callback {
            override fun onItemClickListener(item: BundleCategoriesDTO) {
                val bundle = bundleOf(
                    Pair(KEY_BUNDLE_CATEGORY_ID, item.idBundleCategories?.toString()),
                    Pair(KEY_CATEGORY_NAME, item.categoryName),
                    Pair(KEY_PRIMARY_COLOR_HEX, item.primaryColor),
                    Pair(KEY_SECONDARY_COLOR_HEX,item.secondaryColor)
                )
                findNavController().navigate(R.id.action_bundleCategoriesFragment_to_bundleVPFragment, bundle)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_bundle_categories, R.layout.toolbar_default, true)
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
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter = adapter
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

