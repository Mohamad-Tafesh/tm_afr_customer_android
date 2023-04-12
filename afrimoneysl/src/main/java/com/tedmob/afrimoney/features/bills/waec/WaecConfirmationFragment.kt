package com.tedmob.afrimoney.features.bills.waec

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.databinding.*
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.viewmodel.observeResourceInline
import com.tedmob.afrimoney.ui.viewmodel.observeTransactionSubmit
import com.tedmob.afrimoney.ui.viewmodel.provideNavGraphViewModel
import com.tedmob.afrimoney.util.getText
import com.tedmob.libraries.validators.formValidator
import com.tedmob.libraries.validators.rules.NotEmptyRule
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WaecConfirmationFragment :
    BaseVBFragment<FragmentWaecConfirmationBinding>() {

    private val args by navArgs<WaecConfirmationFragmentArgs>()
    lateinit var fees_: String
    private val viewModel by provideNavGraphViewModel<WaecViewModel>(R.id.nav_waec)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentWaecConfirmationBinding::inflate)
    }

    override fun configureToolbar() {
        actionbar?.show()
        actionbar?.title = ""

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFees(args.type)
        withVBAvailable {

            if (args.type=="1")  header.text=getString(R.string.you_are_about_to_e_register_for_waec)
            observeData()

            val validator = setupValidator()
            confirmButton.setDebouncedOnClickListener { validator.submit(viewLifecycleOwner.lifecycleScope) }
        }

    }

    private fun FragmentWaecConfirmationBinding.observeData() {
        observeResourceInline(viewModel.data, contentLL) {
            fees.text = getString(R.string.amount_currency, it)
            fees_ = it
        }

        observeTransactionSubmit(viewModel.submitted, confirmButton) {
            exit()
        }
    }


    private inline fun FragmentWaecConfirmationBinding.setupValidator() = formValidator {
        pinInputLayout.validate(NotEmptyRule(getString(R.string.mandatory_field)))

        onValid = {
            viewModel.getConfirmation(pinInputLayout.getText(),fees_,args.type)
        }
    }


    private inline fun exit() {
        findNavController().popBackStack(R.id.payMyBillsFragment, false)
    }
}