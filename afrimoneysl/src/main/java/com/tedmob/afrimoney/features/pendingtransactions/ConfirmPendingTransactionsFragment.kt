package com.tedmob.afrimoney.features.pendingtransactions

import android.os.Bundle
import android.view.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.data.api.dto.PendingTransactionsItemDTO
import com.tedmob.afrimoney.databinding.FragmentConfirmPendingTransactionsBinding
import com.tedmob.afrimoney.databinding.FragmentMerchantPaymentConfirmationBinding
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.viewmodel.observeTransactionSubmit
import com.tedmob.afrimoney.ui.viewmodel.provideViewModel
import com.tedmob.afrimoney.util.getText
import com.tedmob.libraries.validators.formValidator
import com.tedmob.libraries.validators.rules.NotEmptyRule
import dagger.hilt.android.AndroidEntryPoint

import javax.inject.Inject


@AndroidEntryPoint
class ConfirmPendingTransactionsFragment :
    BaseVBFragment<FragmentConfirmPendingTransactionsBinding>() {

    @Inject
    lateinit var appSessionNavigator: AppSessionNavigator

    private val viewModel by provideViewModel<PendingTransactionsViewModel>()


    private val args by navArgs<ConfirmPendingTransactionsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentConfirmPendingTransactionsBinding::inflate)
    }


    override fun configureToolbar() {
        actionbar?.show()
        actionbar?.title = ""
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setData(args.data)

        withVBAvailable {
            val validator = setupValidator()
            confirmButton.setDebouncedOnClickListener { validator.submit(viewLifecycleOwner.lifecycleScope) }

            observeTransactionSubmit(viewModel.submitted, confirmButton)
        }


    }


    private fun setData(it: PendingTransactionsItemDTO) {
        withVBAvailable {
            transactionid.text = it.transaction_id
            dateText.text = it.date
            amount.text = context?.getString(R.string.amount_currency, it.amount)
            initiated.text = it.from
        }
    }


    private inline fun FragmentConfirmPendingTransactionsBinding.setupValidator() = formValidator {
        pinInputLayout.validate(NotEmptyRule(getString(R.string.mandatory_field)))

        onValid = {
            viewModel.getConfirmation(
                args.type,
                pinInputLayout.getText(),
                args.data.serviceRequestId.toString()
            )
        }
    }


}