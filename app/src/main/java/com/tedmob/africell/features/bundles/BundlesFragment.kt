package com.tedmob.africell.features.bundles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.data.api.dto.BundleInfo
import com.tedmob.africell.data.api.dto.BundlesDTO
import com.tedmob.africell.features.bundles.BundleDetailsFragment.Companion.BUNDLE_ID

import com.tedmob.africell.ui.viewmodel.provideViewModel
import kotlinx.android.synthetic.main.fragment_bundles.*

class BundlesFragment : BaseFragment() {
    val bundle get() = arguments?.getParcelable<BundlesDTO>(KEY_BUNDLE)


    companion object {
        const val KEY_BUNDLE = "key_bundle"
        fun newInstance(bundle:BundlesDTO ?): BundlesFragment {
            return BundlesFragment().apply {
                arguments = bundleOf(KEY_BUNDLE to bundle)

            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_bundles, 0, false)
    }


    private val viewModel by provideViewModel<BundlesViewModel> {
        viewModelFactory
    }

    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)

    }


    val adapter by lazy {
        BundleAdapter(mutableListOf(),object : BundleAdapter.Callback{
            override fun onItemClickListener(item: BundleInfo) {
              val bundle = bundleOf(BUNDLE_ID to item.bundleId?.toString())
                findNavController().navigate(R.id.action_bundleVPFragment_to_bundleDetailsFragment,bundle)
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupCategoriesItem()

    }


    private fun setupCategoriesItem() {
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = true
        adapter.setItems(bundle?.bundleInfo.orEmpty().toMutableList())
    }



}