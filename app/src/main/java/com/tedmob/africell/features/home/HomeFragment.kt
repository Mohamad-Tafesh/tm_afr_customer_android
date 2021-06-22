package com.tedmob.africell.features.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.api.ApiContract
import com.tedmob.africell.data.api.ApiContract.ImagePageName.HOME_PAGE
import com.tedmob.africell.data.api.ApiContract.Params.SLIDERS
import com.tedmob.africell.data.entity.SubAccount
import com.tedmob.africell.data.repository.domain.SessionRepository
import com.tedmob.africell.data.toHomeBalance
import com.tedmob.africell.features.accountInfo.AccountViewModel
import com.tedmob.africell.features.accountsNumber.AccountsNumbersFragment
import com.tedmob.africell.ui.blocks.showLoading
import com.tedmob.africell.ui.viewmodel.observeResourceInline
import com.tedmob.africell.ui.viewmodel.observeResourceWithoutProgress
import com.tedmob.africell.ui.viewmodel.provideViewModel
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolbar_home.*
import javax.inject.Inject

class HomeFragment : BaseFragment() {
    @Inject
    lateinit var sessionRepository: SessionRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_home, R.layout.toolbar_home, true)
    }

    val balanceAdapter by lazy {
        HomeBalanceAdapter(mutableListOf())
    }


    private val viewModel by provideViewModel<HomeViewModel> { viewModelFactory }
    val handler = Handler(Looper.getMainLooper())
    var runnable: Runnable? = null
    val POST_DELAY = 3000L
    private val infiniteBalanceAdapter by lazy { InfiniteScrollAdapter.wrap(balanceAdapter) }


    private val accountViewModel by provideViewModel<AccountViewModel> { viewModelFactory }
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
        setupImageBanner(toolbarImage, ApiContract.Params.BANNERS, ApiContract.ImagePageName.HOME_PAGE)
        bundlesLayout.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_bundleActivity)
        }
        lineRecharge.setOnClickListener {
            if (sessionRepository.isLoggedIn()) {
                findNavController().navigate(R.id.action_homeFragment_to_lineRechargeFragment)
            } else showLoginMessage()
        }
        accountInfoLayout.setOnClickListener {
            if (sessionRepository.isLoggedIn()) {
                findNavController().navigate(R.id.action_homeFragment_to_accountInfoActivity)
            } else showLoginMessage()
        }
        dataCalculatorBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_dataCalculatorFragment)
        }
        myBundleServicesBtn.setOnClickListener {
            if (sessionRepository.isLoggedIn()) {
                findNavController().navigate(R.id.action_homeFragment_to_myBundleServicesVPFragment)
            } else showLoginMessage()
        }
        vasServicesBtn.setOnClickListener {
            if (sessionRepository.isLoggedIn()) {
                findNavController().navigate(R.id.action_homeFragment_to_vasServicesFragment)
            } else showLoginMessage()
        }

        bindPush()
        //balanceAdapter.setItems(balance)
    }

    private fun bindPush() {
        if (sessionRepository.isLoggedIn()) {
            viewModel.setUserPush()
        }
    }

    override fun onStart() {
        super.onStart()
        setupRecyclerView()
        setupUI()
        bindData()


    }
    private fun setupUI() {
        if (sessionRepository.isLoggedIn()) {
            viewModel.getSubAccounts()
            accountViewModel.getAccountInfo()
            loginTxt.visibility = View.GONE
            enrollBtn.visibility = View.VISIBLE
            enrollBtn.setOnClickListener {
                findNavController().navigate(R.id.addAccountActivity)
            }
        } else {
            showContent()
            loading.showContent()
            loginTxt.visibility = View.VISIBLE
            loginTxt.setOnClickListener {
                showLoginMessage()
            }
            accountSpinner.visibility = View.GONE
            enrollBtn.visibility = View.GONE
        }
    }

    private fun setupRecyclerView() {
        with(recyclerView) {
            adapter = infiniteBalanceAdapter
            setItemTransformer(
                com.yarolegovich.discretescrollview.transform.ScaleTransformer.Builder()
                    .setPivotX(com.yarolegovich.discretescrollview.transform.Pivot.X.CENTER)
                    .setMaxScale(1.1f)
                    //      .setPivotY(com.yarolegovich.discretescrollview.transform.Pivot.Y.TOP)
                    .build()
            )
            setSlideOnFling(true)

        }

    }


    private fun bindData() {


        observeResourceInline(viewModel.subAccountData) { subAccounts ->
            if (sessionRepository.selectedMsisdn.isEmpty()) {
                sessionRepository.selectedMsisdn = subAccounts.get(0).account.orEmpty()
            }
            accountSpinner.setText(sessionRepository.selectedMsisdn)
            accountSpinner.setOnClickListener {
                val bottomSheetFragment = AccountsNumbersFragment.newInstance(ArrayList(subAccounts))
                bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
                bottomSheetFragment.setCallBack(object : AccountsNumbersFragment.Callback {
                    override fun addNewAccount() {
                        findNavController().navigate(R.id.addAccountActivity)
                    }

                    override fun manageAccount() {
                        findNavController().navigate(R.id.accountManagementActivity)
                    }

                    override fun setDefault(subAccount: SubAccount) {
                        accountSpinner.setText(subAccount.account)
                        accountViewModel.getAccountInfo()
                    }
                })
            }
            accountSpinner.visibility = View.VISIBLE

        }


        accountViewModel.accountInfoData?.observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        loading.showLoading()
                    }
                    is Resource.Success -> {

                        loading.showContent()
                        val data = resource.data
                        balanceAdapter.setItems(data.homePage?.toHomeBalance().orEmpty())
                    }
                    is Resource.Error -> {

                        showMessage(resource.message)
                    }
                }
            }
        })


        viewModel.getImages(SLIDERS, HOME_PAGE)
        observeResourceWithoutProgress(viewModel.imagesData) {
            viewPager.adapter = offersAdapter
            offersAdapter.setItems(it)
            pageIndicator.setViewPager(viewPager)
            autoScroll(it.size)
        }
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