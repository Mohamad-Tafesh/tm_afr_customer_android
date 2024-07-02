package com.tedmob.afrimoney.features.africellservices.airtime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.databinding.FragmentAirtimeBinding
import com.tedmob.afrimoney.databinding.FragmentAirtimeConfirmationBinding
import com.tedmob.afrimoney.databinding.FragmentDataBundleConfirmationBinding
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
class AirtimeConfirmationFragment :
    BaseVBFragment<FragmentAirtimeConfirmationBinding>() {
    var datas: AirtimeViewModel.Params? = null
    private val viewModel by provideNavGraphViewModel<AirtimeViewModel>(R.id.nav_airtime)

    companion object {
        var SELF: Int = 0
        var OTHER: Int = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentAirtimeConfirmationBinding::inflate)
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

    private fun FragmentAirtimeConfirmationBinding.observeData() {
        observeResourceInline(viewModel.dataSelf, contentLL) {
            setupData(it)
        }
        observeTransactionSubmit(viewModel.submitted, confirmButton) { exit() }
    }


    private fun FragmentAirtimeConfirmationBinding.setupData(data: AirtimeViewModel.Params) {
        with(data) {
            transferText.text = getString(R.string.amount_currency, transactionAmount)
            toNumberText.text = number
            bundleNameHeaderText.text = bundleName + " " + validity

            datas = data

        }
    }

    private inline fun FragmentAirtimeConfirmationBinding.setupValidator() = formValidator {
        pinInputLayout.validate(NotEmptyRule(getString(R.string.mandatory_field)))

        onValid = {
            if (datas!!.type == SELF) {
                viewModel.getConfirmationSelf(
                    datas!!.remark,
                    datas!!.transactionAmount,
                    pinInputLayout.getText(),
                    datas!!.idValue,
                    datas!!.idType,
                    datas!!.bundle,
                    datas!!.walletID,
                )
            } else if (datas!!.type == OTHER) {
                viewModel.getConfirmationOther(
                    datas!!.number,
                    datas!!.transactionAmount,
                    pinInputLayout.getText(),
                    datas!!.idValue,
                    datas!!.idType,
                    datas!!.bundle,
                    datas!!.walletID,
                )
            }

        }
    }


    private inline fun exit() {
        findNavController().popBackStack(R.id.homeFragment, false)
    }
}