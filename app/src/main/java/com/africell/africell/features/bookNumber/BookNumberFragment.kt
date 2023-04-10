package com.africell.africell.features.bookNumber

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.africell.africell.R
import com.africell.africell.app.viewbinding.BaseVBFragment
import com.africell.africell.app.viewbinding.withVBAvailable
import com.africell.africell.data.Resource
import com.africell.africell.data.repository.domain.SessionRepository
import com.africell.africell.databinding.FragmentBookNumberBinding
import com.africell.africell.databinding.ToolbarDefaultBinding
import com.africell.africell.ui.viewmodel.observe
import com.africell.africell.ui.viewmodel.observeResource
import com.africell.africell.ui.viewmodel.provideViewModel
import com.africell.africell.util.getText
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class BookNumberFragment : BaseVBFragment<FragmentBookNumberBinding>() {

    private val viewModel by provideViewModel<BookNumberViewModel> { viewModelFactory }

    @Inject
    lateinit var sessionRepository: SessionRepository
    val adapter by lazy {
        BookNumberAdapter(mutableListOf(), object : BookNumberAdapter.Callback {
            override fun onBookNumberClickListener(item: String) {
                viewModel.bookNumber(item)
            }

        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(container, FragmentBookNumberBinding::inflate, true, ToolbarDefaultBinding::inflate)
    }

    private fun searchRxTextView() {
        withVBAvailable {
            searchTxt?.let {
                RxTextView.textChanges(it)
                    .skip(1)
                    .debounce(300, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ charSequence ->
                        viewModel.getFreeNumbers(charSequence.toString())
                    }) { t -> }
            }
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
        withVBAvailable {
            if (sessionRepository.isLoggedIn()) {
                setupRecyclerView()
                searchRxTextView()
                viewModel.getFreeNumbers(searchTextLayout.getText())
                bindData()
                showContent()
            } else {
                showInlineMessageWithAction(getString(R.string.login_first), actionName = getString(R.string.login)) {
                    redirectToLogin()
                }
            }
        }
    }


    private fun FragmentBookNumberBinding.setupRecyclerView() {
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
        observe(viewModel.freeNumbersData) { res ->
            withVBAvailable {
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


        observeResource(viewModel.bookNumberData) {
            withVBAvailable {
                viewModel.getFreeNumbers(searchTextLayout.getText())
                showMaterialMessageDialog(
                    getString(R.string.successful),
                    it.resultText ?: "",
                    getString(R.string.close)
                ) {

                }
            }
        }
    }


}

