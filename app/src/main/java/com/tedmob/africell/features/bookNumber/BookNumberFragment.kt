package com.tedmob.africell.features.bookNumber

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationServices
import com.jakewharton.rxbinding2.widget.RxTextView
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.api.dto.BookNumberDTO
import com.tedmob.africell.data.api.dto.LocationDTO
import com.tedmob.africell.features.location.LocationDetailsFragment.Companion.LOCATION_DETAILS
import com.tedmob.africell.ui.viewmodel.ViewModelFactory
import com.tedmob.africell.ui.viewmodel.observe
import com.tedmob.africell.ui.viewmodel.observeResource
import com.tedmob.africell.ui.viewmodel.provideViewModel
import com.tedmob.africell.util.getText
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_location.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class BookNumberFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by provideViewModel<BookNumberViewModel> { viewModelFactory }

    val adapter by lazy {
        BookNumberAdapter(mutableListOf(), object : BookNumberAdapter.Callback {
            override fun onItemClickListener(item: BookNumberDTO) {

            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_book_number, R.layout.toolbar_default, false)
    }

    private fun searchRxTextView() {
        searchTxt?.let {
            RxTextView.textChanges(it)
                .skip(1)
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ charSequence ->
                    viewModel.getBookNumber(charSequence.toString())
                }) { t -> }
        }
    }

    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.title = getString(R.string.book_number)
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_side_menu)
        actionbar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        searchRxTextView()
        viewModel.getBookNumber(searchTextLayout.getText())
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
        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = false
        }
    }

    private fun bindData() {
        observe(viewModel.locationData) { res ->
            when (res) {
                is Resource.Loading -> {
                    swipeRefresh.isRefreshing = true
                }
                is Resource.Error -> {
                    showContent()
                    showMessageWithAction(res.message, getString(R.string.retry), res.action)
                    swipeRefresh.isRefreshing = false
                }
                is Resource.Success -> {
                    showContent()
                    val data = res.data
                    if (res.data.isNullOrEmpty()) {
                        emptyMessage.text = getString(R.string.no_book_available)
                    } else {
                        emptyMessage.text = ""
                    }
                    adapter.setItems(data)
                    swipeRefresh.isRefreshing = false
                }
                else -> {
                    swipeRefresh.isRefreshing = false
                }
            }


        }
    }



}

