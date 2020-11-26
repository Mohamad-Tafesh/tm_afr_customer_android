package com.tedmob.africell.features.bundles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.data.api.dto.BundlesDTO
import com.tedmob.africell.ui.viewmodel.ViewModelFactory
import com.tedmob.africell.ui.viewmodel.observeResourceInline
import com.tedmob.africell.ui.viewmodel.provideViewModel
import kotlinx.android.synthetic.main.fragment_bundles.*
import javax.inject.Inject


class BundlesFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by provideViewModel<BundlesViewModel> { viewModelFactory }

    val adapter by lazy {
        BundlesAdapter(mutableListOf(), object : BundlesAdapter.Callback {
            override fun onItemClickListener(item: BundlesDTO) {

            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_bundles, R.layout.toolbar_default, false)
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
        viewModel.getBundles()
        bindData()
    }


    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter = adapter
    }

    private fun bindData() {
        observeResourceInline(viewModel.bundleData) {
            if (it.isNullOrEmpty()) {
                showInlineMessage(getString(R.string.no_bundle_available))
            }
            adapter.setItems(it)
        }
    }
}

