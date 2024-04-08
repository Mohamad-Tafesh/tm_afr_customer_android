package com.tedmob.afrimoney.features.authentication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.databinding.FragmentChangePinRegisterBinding
import com.tedmob.afrimoney.features.account.ChangePinViewModel
import com.tedmob.afrimoney.features.newhome.AfrimoneyActivity
import com.tedmob.afrimoney.features.newhome.AfrimoneyRegistrationActivity
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.hideKeyboard
import com.tedmob.afrimoney.ui.viewmodel.observeTransactionSubmit
import com.tedmob.afrimoney.ui.viewmodel.provideViewModel
import com.tedmob.afrimoney.util.getText
import com.tedmob.libraries.validators.formValidator
import com.tedmob.libraries.validators.rules.NotEmptyRule
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePinRegisterFragment : BaseVBFragment<FragmentChangePinRegisterBinding>() {


    private val viewModel by provideViewModel<ChangePinViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentChangePinRegisterBinding::inflate)
    }

    override fun configureToolbar() {
        actionbar?.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        withVBAvailable {
            val validator = setupValidation()
            proceedButton.setDebouncedOnClickListener {
                validator.submit(viewLifecycleOwner.lifecycleScope)
            }
        }

        observeTransactionSubmit(
            viewModel.pinChanged,
            requireBinding().proceedButton,
            "Your account has been created successfully"
        ) {

            startActivity(Intent(activity, AfrimoneyActivity::class.java).apply {
                putExtra("number", session.msisdn)
                putExtra("token", session.accessToken)

            })
        }

    }

    private fun FragmentChangePinRegisterBinding.setupValidation() = formValidator {
        oldPin.validate(NotEmptyRule(getString(R.string.mandatory_field)))
        confirmPin.validate(NotEmptyRule(getString(R.string.mandatory_field)))

        onValid = { changePin() }
    }


    private fun changePin() {
        hideKeyboard()
        withVBAvailable {
            if (confirmPin.getText() == newPin.getText())
                viewModel.getConfirmation(oldPin.getText(), confirmPin.getText())
            else {
                confirmPin.error = getString(R.string.pins_do_not_match)
                newPin.error = getString(R.string.pins_do_not_match)
            }

        }
    }



}