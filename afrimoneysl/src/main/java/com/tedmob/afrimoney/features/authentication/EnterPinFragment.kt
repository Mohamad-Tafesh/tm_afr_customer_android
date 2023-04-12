package com.tedmob.afrimoney.features.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.databinding.FragmentEnterPinBinding
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.viewmodel.observeResourceFromButton
import com.tedmob.afrimoney.ui.viewmodel.provideViewModel
import com.tedmob.afrimoney.util.getText
import com.tedmob.libraries.validators.formValidator
import com.tedmob.libraries.validators.rules.NotEmptyRule
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EnterPinFragment : BaseVBFragment<FragmentEnterPinBinding>() {

    private val viewModel by provideViewModel<EnterPinViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentEnterPinBinding::inflate)
    }

    override fun configureToolbar() {
        actionbar?.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        withVBAvailable {
            val validator = setupValidation()
            proceedButton.setDebouncedOnClickListener { validator.submit(viewLifecycleOwner.lifecycleScope) }
        }

        observeResourceFromButton(viewModel.pinEntered, R.id.proceedButton) {
            unblockApp()
        }
    }

    private fun FragmentEnterPinBinding.setupValidation() = formValidator {
        pinInputLayout.validate(NotEmptyRule(getString(R.string.mandatory_field)))

        onValid = {
            viewModel.enterPin(session.msisdn, pinInputLayout.getText())
        }
    }


    private fun unblockApp() {
        activity?.finish()
    }
    /*
              barcodeLayout.showLoading()
                try {
                    val result = suspendForOneSignalUserId()
                    if (result.isNotEmpty()) {
                        barcodeLayout.showContent()
                        setupBarcode(result)
                    } else {
                        throw AppException("OneSignal did not return a valid userId")
                    }
                } catch (e: Exception) {
                    if (e !is AppException) {
                        e.printStackTrace()
                    }
                    barcodeLayout.showMessage("An error occurred. Please restart the app.")
                }
     */
}