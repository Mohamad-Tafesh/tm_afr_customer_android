package com.tedmob.afrimoney.features.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.data.Resource
import com.tedmob.afrimoney.data.entity.UserHomeData
import com.tedmob.afrimoney.databinding.FragmentHomeNewBinding
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.viewmodel.observeResourceInline
import com.tedmob.afrimoney.ui.viewmodel.provideViewModel
import com.tedmob.afrimoney.util.security.StringEncryptor
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class HomeFragment : BaseVBFragment<FragmentHomeNewBinding>() {

    @Inject
    @Named("local-string")
    lateinit var encryptor: StringEncryptor

    @Inject
    lateinit var appSessionNavigator: AppSessionNavigator

    private val viewModel by provideViewModel<HomeViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentHomeNewBinding::inflate, false)
    }

    override fun configureToolbar() {
        actionbar?.show()
        actionbar?.setBackgroundDrawable(resources.getDrawable(R.color.transparent))
        actionbar?.title = ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        withVBAvailable {
            setupToolbar()
            setupOptions()
        }


        observeResourceInline(viewModel.data) {
            setupUser(it)
        }

        viewModel.balance.observe(viewLifecycleOwner) {
            if (it is Resource.Success) {
                it.data.let {


                    val newBalance =
                        (it.balance.toDouble() - it.fbr.toDouble() - it.fic.toDouble()).toString()


                    if ((newBalance.split("."))[1] == "00") {
                        val bal = newBalance.toDoubleOrNull()?.toInt()
                        binding?.balanceText?.text = bal.toString() + "NLe"
                    } else binding?.balanceText?.text = newBalance + "NLe"


                    binding?.userName?.text = it.userName
                }
            }
        }


        requireBinding().transactionsLayout.setOnClickListener {

            findNavController().navigate(HomeFragmentDirections.fromHomeFragmentToEnterPin())
        }



        viewModel.getData()
        viewModel.getBalance()

    }

    private fun setupUser(data: UserHomeData) {
        withVBAvailable {
            data.transactionsCount.let {
                transactionsBadge.isVisible = it > 0
                transactionsBadge.text = it.toString()
            }
        }
    }


    private fun FragmentHomeNewBinding.setupToolbar() {
        actionbar?.title = ""
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_side_menu)
        actionbar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun FragmentHomeNewBinding.setupOptions() {
        transactionsLayout.setDebouncedOnClickListener {
            //findNavController().navigate(R.id.)
        }
        transferMoneyOption.setDebouncedOnClickListener {
            findNavController().navigate(R.id.nav_transfer_money)
        }
        pendingTransactionsOption.setDebouncedOnClickListener {
            findNavController().navigate(R.id.nav_pending_transactions)
        }
        withdrawMoneyOption.setDebouncedOnClickListener {
            findNavController().navigate(R.id.nav_withdraw)
        }
        buyAnytimeOption.setDebouncedOnClickListener {
            findNavController().navigate(R.id.nav_airtime)
        }
        payMyBillsOption.setDebouncedOnClickListener {
            findNavController().navigate(R.id.nav_pay_my_bills)
        }
        merchantPaymentOption.setDebouncedOnClickListener {
            findNavController().navigate(R.id.nav_merchant_payment)
        }
        bankingServicesOption.setDebouncedOnClickListener {
            findNavController().navigate(R.id.nav_banking_services)
        }
    }
}