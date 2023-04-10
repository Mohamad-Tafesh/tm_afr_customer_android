package com.africell.africell.features.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.africell.africell.R
import com.africell.africell.app.viewbinding.BaseVBFragment
import com.africell.africell.app.viewbinding.withVBAvailable
import com.africell.africell.data.Resource
import com.africell.africell.data.api.ApiContract
import com.africell.africell.data.api.ApiContract.ImagePageName.HOME_PAGE
import com.africell.africell.data.api.ApiContract.Params.SLIDERS
import com.africell.africell.data.entity.SubAccount
import com.africell.africell.data.repository.domain.SessionRepository
import com.africell.africell.data.toHomeBalance
import com.africell.africell.databinding.FragmentHomeBinding
import com.africell.africell.databinding.ToolbarHomeBinding
import com.africell.africell.features.accountInfo.AccountViewModel
import com.africell.africell.features.accountsNumber.AccountsNumbersFragment
import com.africell.africell.ui.blocks.showLoading
import com.africell.africell.ui.viewmodel.observeNotNull
import com.africell.africell.ui.viewmodel.observeResourceInline
import com.africell.africell.ui.viewmodel.observeResourceWithoutProgress
import com.africell.africell.ui.viewmodel.provideViewModel
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseVBFragment<FragmentHomeBinding>() {
    @Inject
    lateinit var sessionRepository: SessionRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(container, FragmentHomeBinding::inflate, true, ToolbarHomeBinding::inflate)
    }

    val balanceAdapter by lazy {
        HomeBalanceAdapter(mutableListOf())
    }


    private val viewModel by provideViewModel<HomeViewModel>()
    val handler = Handler(Looper.getMainLooper())
    var runnable: Runnable? = null
    val POST_DELAY = 8000L
    private val infiniteBalanceAdapter by lazy { InfiniteScrollAdapter.wrap(balanceAdapter) }


    private val accountViewModel by provideViewModel<AccountViewModel>()
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
        getToolbarBindingAs<ToolbarHomeBinding>()?.run {
            setupImageBanner(toolbarImage, ApiContract.Params.BANNERS, ApiContract.ImagePageName.HOME_PAGE)
        }
        withVBAvailable {
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
        }

        bindPush()
        //balanceAdapter.setItems(balance)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        bindData()
    }

    override fun onStart() {
        super.onStart()
        setupUI()
    }

    private fun bindPush() {
        if (sessionRepository.isLoggedIn()) {
            viewModel.setUserPush()
        }
    }


    private fun setupUI() {
        withVBAvailable {
            getToolbarBindingAs<ToolbarHomeBinding>()?.run {
                if (sessionRepository.isLoggedIn()) {
                    viewModel.getSubAccounts()

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
        }
    }

    private fun setupRecyclerView() {
        withVBAvailable {
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

    }


    private fun bindData() {


        observeResourceInline(viewModel.subAccountData) { subAccounts ->
            if (sessionRepository.selectedMsisdn.isEmpty() || subAccounts.firstOrNull { it.account == sessionRepository.selectedMsisdn } == null) {
                sessionRepository.selectedMsisdn = subAccounts.get(0).account.orEmpty()
            }
            getToolbarBindingAs<ToolbarHomeBinding>()?.run {
                accountViewModel.getAccountInfo()
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

        }

        observeNotNull(accountViewModel.accountInfoData, {
            withVBAvailable {
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
            }
        })


        viewModel.getImages(SLIDERS, HOME_PAGE)
        observeResourceWithoutProgress(viewModel.imagesData) {
            withVBAvailable {
                viewPager.adapter = offersAdapter
                offersAdapter.setItems(it)
                pageIndicator.setViewPager(viewPager)
                autoScroll(it.size)
            }
        }
    }


    private fun autoScroll(size: Int) {
        runnable = Runnable {
            withVBAvailable {
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
        }
        start()
    }

    private fun start() {
        stop()
        runnable?.let {
            handler.postDelayed(it, POST_DELAY)
        }
    }

    private fun stop() {
        runnable?.let {
            handler.removeCallbacks(it)
        }
    }

    override fun onDestroyView() {
        runnable?.let {
            handler.removeCallbacks(it)
        }
        super.onDestroyView()
    }

}