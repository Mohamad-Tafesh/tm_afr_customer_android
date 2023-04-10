package com.africell.africell.features.afrimoney

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.africell.africell.R
import com.africell.africell.app.BaseActivity
import com.africell.africell.app.viewbinding.BaseVBFragment
import com.africell.africell.app.viewbinding.withVBAvailable
import com.africell.africell.data.entity.afrimoney.HomeData
import com.africell.africell.data.repository.domain.SessionRepository
import com.africell.africell.databinding.FragmentAfriMoneySlBinding
import com.africell.africell.ui.button.setDebouncedOnClickListener
import com.africell.africell.ui.viewmodel.observeResourceInline
import com.africell.africell.ui.viewmodel.provideViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AfrimoneySLFragment : BaseVBFragment<FragmentAfriMoneySlBinding>() {

    @Inject
    lateinit var session: SessionRepository

    private val viewModel by provideViewModel<AfrimoneySLViewModel>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(container, FragmentAfriMoneySlBinding::inflate, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        withVBAvailable {
            setupToolbar()
            setupOptions()
        }

        bindData()

        viewModel.getData()
    }

    private fun FragmentAfriMoneySlBinding.setupToolbar() {
        (activity as? BaseActivity?)?.setSupportActionBar(toolbarContainer.toolbar)

        actionbar?.title = ""
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_side_menu)
        actionbar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun FragmentAfriMoneySlBinding.setupOptions() {
        transactionsLayout.setDebouncedOnClickListener {
            //...
        }
        transferMoneyOption.setDebouncedOnClickListener {
            //...
        }
        pendingTransactionsOption.setDebouncedOnClickListener {
            //...
        }
        withdrawMoneyOption.setDebouncedOnClickListener {
            //...
        }
        buyAnytimeOption.setDebouncedOnClickListener {
            //...
        }
        payMyBillsOption.setDebouncedOnClickListener {
            //...
        }
        merchantPaymentOption.setDebouncedOnClickListener {
            //...
        }
        bankingServicesOption.setDebouncedOnClickListener {
            //...
        }
    }

    private fun bindData() {
        observeResourceInline(viewModel.data) {
            withVBAvailable {
                setupUserInfo(it)
            }
        }
    }


    private fun FragmentAfriMoneySlBinding.setupUserInfo(data: HomeData) {
        data.bonusBalance.let {
            bonusBalanceText.text = it
        }
        data.balance.let {
            balanceText.text = it
        }
        data.transactionsCount.let {
            transactionsBadge.isVisible = it > 0
            transactionsBadge.text = it.toString()
        }
    }
}