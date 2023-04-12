package com.tedmob.afrimoney.features.bills.edsa

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
import com.tedmob.afrimoney.databinding.*
import com.tedmob.afrimoney.ui.applySheetEffectTo
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.viewmodel.observeResource
import com.tedmob.afrimoney.ui.viewmodel.observeResourceInline
import com.tedmob.afrimoney.ui.viewmodel.observeTransactionSubmit
import com.tedmob.afrimoney.ui.viewmodel.provideNavGraphViewModel
import com.tedmob.afrimoney.util.getText
import com.tedmob.libraries.validators.formValidator
import com.tedmob.libraries.validators.rules.NotEmptyRule
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EdsaConfirmationFragment :
    BaseVBFragment<FragmentEdsaConfirmationBinding>() {

    private val viewModel by provideNavGraphViewModel<EdsaViewModel>(R.id.nav_edsa)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentEdsaConfirmationBinding::inflate)
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

    private fun FragmentEdsaConfirmationBinding.observeData() {
        withVBAvailable {
        observeResource(viewModel.data){

                setupData(it)

        }
       observeTransactionSubmit(viewModel.submitted, confirmButton) {
           exit()
       }
       }
    }


    private fun FragmentEdsaConfirmationBinding.setupData(data: EdsaViewModel.EdsaData) {
        with(data) {
            meterNumber.text = data.number
            amountText.text= getString(R.string.amount_currency,data.amount)
            totalText.text=getString(R.string.amount_currency,data.amount)

        }
    }

    private inline fun FragmentEdsaConfirmationBinding.setupValidator() = formValidator {
        pinInputLayout.validate(NotEmptyRule(getString(R.string.mandatory_field)))

        onValid = {

           viewModel.getConfirmation(pinInputLayout.getText())
        }
    }


    private inline fun exit() {
        findNavController().popBackStack(R.id.payMyBillsFragment, false)
    }
}