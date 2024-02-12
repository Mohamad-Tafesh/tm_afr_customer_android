package com.tedmob.afrimoney.features.banking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.databinding.FragmentWalletToBankBinding
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.viewmodel.observeResource
import com.tedmob.afrimoney.ui.viewmodel.provideNavGraphViewModel
import com.tedmob.afrimoney.util.getText
import com.tedmob.libraries.validators.FormValidator
import com.tedmob.libraries.validators.formValidator
import com.tedmob.libraries.validators.rules.DoubleRule
import com.tedmob.libraries.validators.rules.NotEmptyRule
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WalletToBankFragment : BaseVBFragment<FragmentWalletToBankBinding>() {

    private val args by navArgs<BankToWalletFragmentArgs>()
    private val viewModel by provideNavGraphViewModel<WalletToBankViewModel>(R.id.nav_banking_services)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentWalletToBankBinding::inflate, false)
    }

    override fun configureToolbar() {
        actionbar?.show()
        actionbar?.title = ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.clear()
        viewModel.selectedBankId = args.bank.bankid

        bindData()


    }

    private inline fun bindData() {

            withVBAvailable {


                wallet.adapter = ArrayAdapter(
                    requireContext(),
                    R.layout.support_simple_spinner_dropdown_item,
                    resources.let {
                        buildList { add("Normal") }
                    }
                )


                val validator = setupValidator()
                proceedButton.setDebouncedOnClickListener { validator.submit(viewLifecycleOwner.lifecycleScope) }
            }


        observeResource(viewModel.proceedToConfirm) {
            viewModel.getFees(args.bank,requireBinding().amountInput.getText())
            proceed()
        }
    }


    private fun FragmentWalletToBankBinding.setupValidator(): FormValidator = formValidator {
        val notEmptyRule = NotEmptyRule(getString(R.string.mandatory_field))

       // wallet.validate(notEmptyRule)
        amountInput.validate(
            notEmptyRule,
            DoubleRule(getString(R.string.invalid_amount))
        )

        onValid = {
            viewModel.proceed(
               wallet.getText(),
                amountInput.getText().toDoubleOrNull() ?: 0.0,
            )
        }
    }


    private fun proceed() {
        observeResource(viewModel.data){
            findNavController().navigate(
                WalletToBankFragmentDirections.actionWalletToBankFragmentToWalletToBankConfirmationFragment(args.bank)
            )
        }

    }
}