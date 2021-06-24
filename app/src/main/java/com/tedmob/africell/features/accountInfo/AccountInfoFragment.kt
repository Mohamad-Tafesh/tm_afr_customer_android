package com.tedmob.africell.features.accountInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.data.api.ApiContract
import com.tedmob.africell.data.api.ApiContract.Params.PREPAID
import com.tedmob.africell.data.entity.SubAccount
import com.tedmob.africell.data.repository.domain.SessionRepository
import com.tedmob.africell.data.toAccountBalanceCategories
import com.tedmob.africell.features.accountsNumber.AccountsNumbersFragment
import com.tedmob.africell.ui.viewmodel.observeResourceInline
import com.tedmob.africell.ui.viewmodel.observeResourceWithoutProgress
import com.tedmob.africell.ui.viewmodel.provideViewModel
import kotlinx.android.synthetic.main.fragment_account_info.*
import kotlinx.android.synthetic.main.toolbar_home.*
import javax.inject.Inject

class AccountInfoFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_account_info, R.layout.toolbar_home, true)
    }

    @Inject
    lateinit var sessionRepository: SessionRepository


    private val viewModel by provideViewModel<AccountViewModel> { viewModelFactory }

    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.title = ""
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_back)
        actionbar?.setDisplayHomeAsUpEnabled(true)
        myProfile.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_editProfileFragment)
        }
        creditTransfer.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_creditTransferFragment)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupImageBanner(toolbarImage, ApiContract.Params.BANNERS, ApiContract.ImagePageName.ACCOUNT_INFO)
        //setupRecyclerView()
        bindData()
        setUpUI()
    }

    private fun setUpUI() {
        loginTxt.visibility = View.GONE
        enrollBtn.visibility = View.VISIBLE
        viewModel.getSubAccounts()
        enrollBtn.setOnClickListener {
            findNavController().navigate(R.id.addAccountActivity)
        }
    }

    private fun bindData() {


        observeResourceWithoutProgress(viewModel.subAccountData) { subAccounts ->
            if (sessionRepository.selectedMsisdn.isEmpty() || subAccounts.firstOrNull { it.account== sessionRepository.selectedMsisdn}==null) {
                sessionRepository.selectedMsisdn = subAccounts.get(0).account.orEmpty()
            }
            accountSpinner.setText(sessionRepository.selectedMsisdn)
            viewModel.getAccountInfo()
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
                        accountSpinner.text = subAccount.account
                        subAccount.account?.let { account -> viewModel.getAccountInfo() }
                    }
                })
                subAccounts[0].account?.let { subAccount -> viewModel.getAccountInfo() }
            }
            accountSpinner.visibility = View.VISIBLE
        }

        observeResourceInline(viewModel.accountInfoData) {
            balanceCategoriesRecyclerView.adapter = AccountBalanceCategoriesAdapter(it.toAccountBalanceCategories())
            helloTxt.text = String.format(getString(R.string.hello_name), sessionRepository.user?.firstName)
            accountType.text = it.accountType
            currentBalance.text = it.balance?.currentBalance + it.balance?.unit

            balanceTitle?.text = it.balance?.title
            balanceCategoriesRecyclerView.adapter = AccountBalanceCategoriesAdapter(it.toAccountBalanceCategories())
            freeBalanceRecyclerView.adapter = FreeBalanceAdapter(
                it.freeBalance?.listFreeBalance.orEmpty().toMutableList()
            )
            freeBalanceTxt.text = it.freeBalance?.title

            postpaidLayout.visibility = if (it.accountType != PREPAID) View.VISIBLE else View.GONE
            prepaidLayout.visibility = if (it.accountType == PREPAID) View.VISIBLE else View.GONE


        }

    }
}