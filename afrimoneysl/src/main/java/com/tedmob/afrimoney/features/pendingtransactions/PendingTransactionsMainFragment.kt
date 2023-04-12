package com.tedmob.afrimoney.features.pendingtransactions

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.navigation.fragment.findNavController
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.data.entity.Bank
import com.tedmob.afrimoney.databinding.*
import com.tedmob.afrimoney.features.authentication.LoginFragmentDirections
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.viewmodel.observeResourceInline
import com.tedmob.afrimoney.ui.viewmodel.provideViewModel
import com.tedmob.afrimoney.util.adapter.AdapterHolderVB
import com.tedmob.afrimoney.util.adapter.adapter
import com.tedmob.afrimoney.util.navigation.runIfFrom
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PendingTransactionsMainFragment : BaseVBFragment<FragmentPendingTransactionsMainBinding>() {



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentPendingTransactionsMainBinding::inflate)


    }


    override fun configureToolbar() {
        actionbar?.show()
        actionbar?.title = ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.cashout.setOnClickListener() {
            findNavController().navigate(PendingTransactionsMainFragmentDirections.actionPendingTransactionsMainFragmentToEnterPinFragment("CASHOUT"))

        }

        binding!!.merchantpayment.setOnClickListener() {
            findNavController().navigate(PendingTransactionsMainFragmentDirections.actionPendingTransactionsMainFragmentToEnterPinFragment("MERCHPAY"))

        }

    }

}