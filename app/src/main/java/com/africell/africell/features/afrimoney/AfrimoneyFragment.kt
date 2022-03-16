package com.africell.africell.features.afrimoney

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.africell.africell.BuildConfig
import com.africell.africell.R
import com.africell.africell.app.BaseFragment
import com.africell.africell.data.Resource
import com.africell.africell.data.api.ApiContract
import com.africell.africell.data.api.ApiContract.ImagePageName.HOME_PAGE
import com.africell.africell.data.api.ApiContract.Params.SLIDERS
import com.africell.africell.data.entity.MoneyTransferOptions
import com.africell.africell.data.entity.SubAccount
import com.africell.africell.data.repository.domain.SessionRepository
import com.africell.africell.features.accountsNumber.AccountsNumbersFragment
import com.africell.africell.features.home.OffersBannerAdapter
import com.africell.africell.features.transferMoney.TransferMoneyFragment
import com.africell.africell.ui.blocks.showImage
import com.africell.africell.ui.blocks.showLoading
import com.africell.africell.ui.viewmodel.observeNotNull
import com.africell.africell.ui.viewmodel.observeResourceInline
import com.africell.africell.ui.viewmodel.observeResourceWithoutProgress
import com.africell.africell.ui.viewmodel.provideViewModel
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter
import kotlinx.android.synthetic.main.fragment_afri_money.*
import kotlinx.android.synthetic.main.toolbar_home.*
import javax.inject.Inject

class AfrimoneyFragment : BaseFragment() {
    @Inject
    lateinit var sessionRepository: SessionRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_afri_money, R.layout.toolbar_home, true)
    }

    val balanceAdapter by lazy {
        AfrimoneyBalanceAdapter(mutableListOf())
    }


    private val viewModel by provideViewModel<AfrimoneyViewModel> { viewModelFactory }
    val handler = Handler(Looper.getMainLooper())
    var runnable: Runnable? = null
    val POST_DELAY = 8000L
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
        setupImageBanner(toolbarImage, ApiContract.Params.BANNERS, ApiContract.ImagePageName.HOME_PAGE)

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


    private fun setupUI() {
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
            if (sessionRepository.selectedMsisdn.isEmpty() || subAccounts.firstOrNull { it.account == sessionRepository.selectedMsisdn } == null) {
                sessionRepository.selectedMsisdn = subAccounts.get(0).account.orEmpty()
            }
            viewModel.getAccountInfo()
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
                        viewModel.getAccountInfo()
                    }
                })
            }
            accountSpinner.visibility = View.VISIBLE

        }

        observeNotNull(viewModel.accountInfoData) {
            it?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        loading.showLoading()
                    }
                    is Resource.Success -> {
                        val data = resource.data
                        balanceAdapter.setItems(data)

                        if (resource.data.isNullOrEmpty()) {
                            loading.showImage(R.drawable.place_holder_afrimoney,"You don't have any afrimoney wallet")
                        } else {
                            loading.showContent()
                            val options = if (BuildConfig.FLAVOR.equals("gambia")) {
                                mutableListOf(
                                    MoneyTransferOptions(
                                        MoneyTransferOptions.IDS.MONEY_TRANSFER,
                                        R.drawable.afrimoney_money_transfer,
                                        getString(R.string.money_n_tranfer)
                                    ),
                                    MoneyTransferOptions(
                                        MoneyTransferOptions.IDS.AFRI_POWER,
                                        R.drawable.afrimoney_afri_power,
                                        getString(R.string.afri_n_power)
                                    ),
                                    MoneyTransferOptions(
                                        MoneyTransferOptions.IDS.LINE_RECHARGE,
                                        R.drawable.afrimoney_line_recharge,
                                        getString(R.string.line_n_recharge)
                                    ),
                                    MoneyTransferOptions(
                                        MoneyTransferOptions.IDS.BUNDLES,
                                        R.drawable.afrimoney_bundle,
                                        getString(R.string.bundles)
                                    ),

                                    )
                            } else
                                mutableListOf(
                                    MoneyTransferOptions(
                                        MoneyTransferOptions.IDS.P2P,
                                        R.drawable.afrimoney_money_transfer,
                                        getString(R.string.p2p_n_tranfer)
                                    ),
                                    MoneyTransferOptions(
                                        MoneyTransferOptions.IDS.MERCHANT_PAYMENT,
                                        R.drawable.afrimoney_afri_power,
                                        getString(R.string.merchant_n_payment)
                                    ),
                                    MoneyTransferOptions(
                                        MoneyTransferOptions.IDS.LINE_RECHARGE,
                                        R.drawable.afrimoney_line_recharge,
                                        getString(R.string.line_n_recharge)
                                    ),
                                    MoneyTransferOptions(
                                        MoneyTransferOptions.IDS.BUNDLES,
                                        R.drawable.afrimoney_bundle,
                                        getString(R.string.bundles)
                                    )
                                )
                            optionsRecyclerView.adapter = AfrimoneyOptionsAdapter(options) {
                                when (it.id) {
                                    MoneyTransferOptions.IDS.P2P -> {
                                        findNavController().navigate(R.id.action_afrimoneyFragment_to_afrimoneyP2PFragment)
                                    }
                                    MoneyTransferOptions.IDS.MERCHANT_PAYMENT -> {
                                        findNavController().navigate(R.id.action_afrimoneyFragment_to_afrimoneyMerchantPayFragment)
                                    }
                                    MoneyTransferOptions.IDS.LINE_RECHARGE -> {
                                        findNavController().navigate(R.id.action_afrimoneyFragment_to_afrimoneyLineRechargeFragment)
                                    }
                                    MoneyTransferOptions.IDS.BUNDLES -> {
                                        findNavController().navigate(R.id.action_afrimoneyFragment_to_afrimoneyBundleActivity)
                                    }
                                    MoneyTransferOptions.IDS.MONEY_TRANSFER -> {
                                        val bottomSheetFragment = TransferMoneyFragment.newInstance()
                                        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
                                        bottomSheetFragment.setCallBack(object : TransferMoneyFragment.Callback {
                                            override fun p2pTransfer() {
                                                findNavController().navigate(R.id.action_afrimoneyFragment_to_afrimoneyP2PFragment)
                                            }

                                            override fun merchantTransfer() {
                                                findNavController().navigate(R.id.action_afrimoneyFragment_to_afrimoneyMerchantPayFragment)
                                            }
                                        })
                                    }
                                    MoneyTransferOptions.IDS.AFRI_POWER -> {

                                    }
                                }
                            }
                        }
                    }
                    is Resource.Error -> {

                        showMessage(resource.message)
                    }
                }
            }
        }


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