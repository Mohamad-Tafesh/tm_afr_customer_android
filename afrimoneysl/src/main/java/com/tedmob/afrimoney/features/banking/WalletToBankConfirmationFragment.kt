package com.tedmob.afrimoney.features.banking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.debugOnly
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.data.entity.GetFeesData
import com.tedmob.afrimoney.databinding.FragmentBankToWalletConfirmationBinding
import com.tedmob.afrimoney.databinding.FragmentWalletToBankConfirmationBinding
import com.tedmob.afrimoney.ui.applySheetEffectTo
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.viewmodel.observeResourceInline
import com.tedmob.afrimoney.ui.viewmodel.observeTransactionSubmit
import com.tedmob.afrimoney.ui.viewmodel.provideNavGraphViewModel
import com.tedmob.afrimoney.util.getText
import com.tedmob.afrimoney.util.setText
import com.tedmob.libraries.validators.formValidator
import com.tedmob.libraries.validators.rules.NotEmptyRule

class WalletToBankConfirmationFragment : BaseVBFragment<FragmentWalletToBankConfirmationBinding>() {

    private val viewModel by provideNavGraphViewModel<BankToWalletViewModel>(R.id.nav_banking_services)

    private val args by navArgs<BankToWalletConfirmationFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(container, FragmentWalletToBankConfirmationBinding::inflate)
    }

    override fun configureToolbar() {
        actionbar?.show()
        actionbar?.title = ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        withVBAvailable {
            scrollParent.applySheetEffectTo(image, icon, iconText)
            observeData()

            debugOnly { pinInputLayout.setText(R.string.debug_password) }

            val validator = setupValidator()
            confirmButton.setDebouncedOnClickListener { validator.submit(viewLifecycleOwner.lifecycleScope) }
        }


    }

    private fun FragmentWalletToBankConfirmationBinding.observeData() {
        observeResourceInline(viewModel.data, contentLL) {
         setupData(it)
        }
        observeTransactionSubmit(viewModel.submitted, confirmButton) {
            exit()
        }
    }


    private fun FragmentWalletToBankConfirmationBinding.setupData(data: GetFeesData) {
        with(data) {
            amounttext.text =getString(R.string.amount_currency,amount)
            toWallet.text = data.number
            feesText.text = getString(R.string.fees_new, data.fees)
            totalText.text = getString(R.string.amount_currency,total)
        }
    }

    private inline fun FragmentWalletToBankConfirmationBinding.setupValidator() = formValidator {
        pinInputLayout.validate(NotEmptyRule(getString(R.string.mandatory_field)))

        onValid = {
            viewModel.confirm(args.bank.accnum,args.bank.bankid,pinInputLayout.getText())
        }
    }


    private inline fun exit() {
        findNavController().popBackStack(R.id.bankingServicesFragment, false)
    }
}