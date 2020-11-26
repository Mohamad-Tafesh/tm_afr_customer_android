package com.tedmob.africell.features.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.data.api.dto.BalanceDTO
import com.tedmob.africell.ui.viewmodel.ViewModelFactory
import com.tedmob.africell.ui.viewmodel.provideViewModel
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class HomeFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_home, R.layout.toolbar_home, false)
    }

    val balanceAdapter by lazy {
        BalanceAdapter(mutableListOf())
    }
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by provideViewModel<HomeViewModel> { viewModelFactory }
    val handler = Handler(Looper.getMainLooper())
    var runnable: Runnable? = null
    val POST_DELAY = 3000L
    private val infiniteBalanceAdapter by lazy { InfiniteScrollAdapter.wrap(balanceAdapter) }

    private val offersAdapter by lazy {
        OffersBannerAdapter(mutableListOf(), object : OffersBannerAdapter.Callback {
            override fun onItemClick(item: String) {
                /*val args = bundleOf("product" to item)
                findNavController().navigate(R.id.action_insuranceFragment_to_insuranceDetailsActivity, args)
           */
            }
        })
    }

    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.title = ""
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_side_menu)
        actionbar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bundleLayout.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_bundleActivity)
        }
        setupRecyclerView()
        bindData()
        val balance = mutableListOf(
            BalanceDTO(1, "2"), BalanceDTO(1, "2"), BalanceDTO(1, "2"),
            BalanceDTO(1, "2"), BalanceDTO(1, "2"), BalanceDTO(1, "2")
        )

        balanceAdapter.setItems(balance)
    }

    private fun setupRecyclerView() {
        with(recyclerView) {
            adapter = infiniteBalanceAdapter
            setItemTransformer(
                com.yarolegovich.discretescrollview.transform.ScaleTransformer.Builder()
                    .setPivotX(com.yarolegovich.discretescrollview.transform.Pivot.X.CENTER)
                    .setMaxScale(1.2f)
                    //      .setPivotY(com.yarolegovich.discretescrollview.transform.Pivot.Y.TOP)
                    .build()
            )
            setSlideOnFling(true)
        }

    }

    private fun bindData() {
/*
        observeResourceInline(viewModel.homeData) {
*/
        viewPager.adapter = offersAdapter
        val banners = mutableListOf<String>("1")
        offersAdapter.setItems(banners)
        pageIndicator.setViewPager(viewPager)
        autoScroll(banners.size)


    }


    private fun autoScroll(size: Int) {
        runnable = Runnable {
            if (viewPager?.scrollState == ViewPager2.SCROLL_STATE_IDLE) {
                val currentPage: Int = viewPager.currentItem
                if (currentPage == size - 1) {
                    viewPager.currentItem = 0
                    viewPager.setCurrentItem(0, true)
                } else {
                    //The second parameter ensures smooth scrolling
                    viewPager.setCurrentItem(currentPage + 1, true)
                }
            }
            handler.postDelayed(runnable!!, POST_DELAY)
        }
        start()
    }

    private fun start() {
        stop()
        runnable?.let {
            handler?.postDelayed(it, POST_DELAY)
        }

    }

    private fun stop() {
        runnable?.let {
            handler?.removeCallbacks(it)
        }
    }

}