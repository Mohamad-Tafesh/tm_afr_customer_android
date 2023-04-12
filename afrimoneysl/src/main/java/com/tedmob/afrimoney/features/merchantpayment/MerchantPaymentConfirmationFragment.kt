package com.tedmob.afrimoney.features.merchantpayment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.databinding.FragmentMerchantPaymentConfirmationBinding
import com.tedmob.afrimoney.ui.applySheetEffectTo
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.viewmodel.observeResourceInline
import com.tedmob.afrimoney.ui.viewmodel.observeTransactionSubmit
import com.tedmob.afrimoney.ui.viewmodel.provideNavGraphViewModel
import com.tedmob.afrimoney.util.getText
import com.tedmob.libraries.validators.formValidator
import com.tedmob.libraries.validators.rules.NotEmptyRule
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MerchantPaymentConfirmationFragment :
    BaseVBFragment<FragmentMerchantPaymentConfirmationBinding>() {

    private val viewModel by provideNavGraphViewModel<MerchantPaymentViewModel>(R.id.nav_merchant_payment)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentMerchantPaymentConfirmationBinding::inflate)
    }

    override fun configureToolbar() {
        actionbar?.show()
        actionbar?.title = ""

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        withVBAvailable {
            scrollParent.applySheetEffectTo(image)
            observeData()

            val validator = setupValidator()
            confirmButton.setDebouncedOnClickListener { validator.submit(viewLifecycleOwner.lifecycleScope) }
        }

    }

    private fun FragmentMerchantPaymentConfirmationBinding.observeData() {
        observeResourceInline(viewModel.data, contentLL) {
            setupData(it)
        }
       observeTransactionSubmit(viewModel.submitted, confirmButton) {
           exit()
       }
    }


    private fun FragmentMerchantPaymentConfirmationBinding.setupData(data: MerchantPaymentData) {
        with(data) {
            transferText.text = getString(R.string.amount_currency,amount)
            if (viewModel.refId!=""){  referenceId.text=viewModel.refId}else{
                referenceIdHeader.isVisible=false
            }
            toMerchantCodeText.text = merchantCode
            feesText.text = getString(R.string.fees_new,  data.fees)
            totalText.text = getString(R.string.amount_currency,total)
            nameText.text = name

        }
    }

    private inline fun FragmentMerchantPaymentConfirmationBinding.setupValidator() = formValidator {
        pinInputLayout.validate(NotEmptyRule(getString(R.string.mandatory_field)))

        onValid = {
            viewModel.getConfirmation(pinInputLayout.getText())
        }
    }


    private inline fun exit() {
        findNavController().popBackStack(R.id.merchantPaymentFragment, false)
    }
}