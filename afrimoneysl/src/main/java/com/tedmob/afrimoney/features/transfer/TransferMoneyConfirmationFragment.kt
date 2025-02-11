package com.tedmob.afrimoney.features.transfer

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
import com.tedmob.afrimoney.data.entity.GetFeesData
import com.tedmob.afrimoney.databinding.FragmentTransferMoneyConfirmationBinding
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
class TransferMoneyConfirmationFragment :
    BaseVBFragment<FragmentTransferMoneyConfirmationBinding>() {

    private val viewModel by provideNavGraphViewModel<TransferMoneyViewModel>(R.id.nav_transfer_money)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentTransferMoneyConfirmationBinding::inflate)
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
            contentLL.showContent()
            setupData()

            val validator = setupValidator()
            confirmButton.setDebouncedOnClickListener { validator.submit(viewLifecycleOwner.lifecycleScope) }
        }

    }

    private fun FragmentTransferMoneyConfirmationBinding.observeData() {
        observeTransactionSubmit(viewModel.submitted, confirmButton) {
            exit()
        }
    }


    private fun FragmentTransferMoneyConfirmationBinding.setupData() {
        viewModel.feesData?.let {
            with(it) {

                if (it.number.isNullOrEmpty()){
                    toNumberText.isVisible=false
                    toNumberHeader.isVisible=false
                    receiverName.isVisible=false
                    nameText.isVisible=false
                    totalHeader.isVisible=false
                    totalText.isVisible=false
                    feesText.isVisible=false

                    transferText.text = getString(R.string.amount_currency,amount)

                }else{
                    transferText.text = getString(R.string.amount_currency,amount)
                    toNumberText.text = number
                    feesText.text = getString(R.string.fees_new, fees)
                    totalText.text =  getString(R.string.amount_currency,total)
                    nameText.text = name.takeIf { it?.isNotBlank()==true }
                }


            }
        }

    }

    private inline fun FragmentTransferMoneyConfirmationBinding.setupValidator() = formValidator {
        pinInputLayout.validate(NotEmptyRule(getString(R.string.mandatory_field)))

        onValid = {
            viewModel.getConfirmation(pinInputLayout.getText())
        }
    }


    private inline fun exit() {
        findNavController().popBackStack(R.id.transferMoneyFragment, false)
    }
}