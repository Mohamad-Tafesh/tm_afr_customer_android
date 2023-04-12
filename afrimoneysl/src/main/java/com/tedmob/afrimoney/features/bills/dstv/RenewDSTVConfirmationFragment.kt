package com.tedmob.afrimoney.features.bills.dstv

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
import com.tedmob.afrimoney.data.Resource
import com.tedmob.afrimoney.databinding.FragmentMerchantPaymentConfirmationBinding
import com.tedmob.afrimoney.databinding.FragmentRenewDstvConfirmationBinding
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
class RenewDSTVConfirmationFragment :
    BaseVBFragment<FragmentRenewDstvConfirmationBinding>() {

    private val viewModel by provideNavGraphViewModel<RenewDSTVViewModel>(R.id.nav_dstv)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentRenewDstvConfirmationBinding::inflate)
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

    private fun FragmentRenewDstvConfirmationBinding.observeData() {
        (viewModel.proceedToConfirm.value as? Resource.Success?)
            ?.let {
                setupData(it.data)
            }
        observeTransactionSubmit(viewModel.submitted, confirmButton) {
            exit()
        }
    }


    private fun FragmentRenewDstvConfirmationBinding.setupData(data: RenewDSTVViewModel.Params) {
        with(data) {
            smartCardText.text = data.cardNumber
            presstext.text = data.press
            typeText.text = data.subType
            nbOfMonthsText.text = data.nbOfMonths
            totalText.text = getString(R.string.amount_currency, data.amount)

        }
    }

    private inline fun FragmentRenewDstvConfirmationBinding.setupValidator() = formValidator {
        pinInputLayout.validate(NotEmptyRule(getString(R.string.mandatory_field)))

        onValid = {
            viewModel.getConfirmation(
                smartCardText.text.toString(),
                nbOfMonthsText.text.toString(),
                totalText.text.toString(),
                pinInputLayout.getText()
            )
        }
    }


    private inline fun exit() {
        findNavController().popBackStack(R.id.payMyBillsFragment, false)
    }
}