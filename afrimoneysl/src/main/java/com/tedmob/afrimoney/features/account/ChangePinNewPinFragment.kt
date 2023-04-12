package com.tedmob.afrimoney.features.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.databinding.DialogTransactionResultBinding
import com.tedmob.afrimoney.databinding.FragmentChangePinNewPinBinding
import com.tedmob.afrimoney.ui.button.observeInView
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.hideKeyboard
import com.tedmob.afrimoney.ui.viewmodel.observeTransactionSubmit
import com.tedmob.afrimoney.ui.viewmodel.provideNavGraphViewModel
import com.tedmob.afrimoney.util.getText
import com.tedmob.libraries.validators.formValidator
import com.tedmob.libraries.validators.rules.NotEmptyRule
import com.tedmob.libraries.validators.rules.Rule
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePinNewPinFragment : BaseVBFragment<FragmentChangePinNewPinBinding>() {

    private val viewModel by provideNavGraphViewModel<ChangePinViewModel>(R.id.nav_change_pin)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentChangePinNewPinBinding::inflate, false)
    }

    override fun configureToolbar() {
        actionbar?.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        withVBAvailable {
            backButton.setDebouncedOnClickListener {
                hideKeyboard()
                findNavController().popBackStack()
            }

            val validator = setupValidator()
            confirmButton.setDebouncedOnClickListener {
                hideKeyboard()
                validator.submit(viewLifecycleOwner.lifecycleScope)
            }
        }

        bindEvents()
    }

    private fun FragmentChangePinNewPinBinding.setupValidator() = formValidator {
        val notEmptyRule = NotEmptyRule(getString(R.string.mandatory_field))

        newPinInput.validate(notEmptyRule)
        confirmNewPinInput.validate(
            notEmptyRule,
            /*,
            object : Rule<CharSequence> {
                override val errorMessage: String = getString(R.string.pins_do_not_match)

                override suspend fun isValid(value: CharSequence): Boolean {
                    return value == newPinInput.getText()
                }
            }*/
        )

        onValid = { changePin() }
    }


    private fun changePin() {
        withVBAvailable {
            if (confirmNewPinInput.getText()
                    .equals(newPinInput.getText())
            ) viewModel.getConfirmation(viewModel.oldpin, newPinInput.getText())
            else {
                confirmNewPinInput.error = getString(R.string.pins_do_not_match)
                newPinInput.error = getString(R.string.pins_do_not_match)
            }

        }
    }

    private fun bindEvents() {

        observeTransactionSubmit(viewModel.pinChanged, requireBinding().confirmButton) {
            findNavController().popBackStack()
        }
/*
viewModel.pinChanged.observeInView(this, requireBinding().confirmButton) {
       onError = { message, _ ->
           showMaterialMessageDialog(message)
       }

       onSuccess = {
           withVBAvailable {
               showSuccess()
           }
       }
   }*/
    }

    private fun showSuccess() {
        activity?.let {
            val viewBinding =
                DialogTransactionResultBinding.inflate(it.layoutInflater, FrameLayout(it), false)

            viewBinding.run {
                resultImage.setImageResource(R.drawable.ic_transaction_success)
                resultTitle.setText(R.string.successful)
                resultMessage.text = getString(R.string.your_pin_has_been_successfully_changed)
            }

            val dialog = MaterialAlertDialogBuilder(it)
                .setView(viewBinding.root)
                .setOnDismissListener {
                    findNavController().popBackStack(R.id.changePinFragment, true)
                }
                .show()

            viewBinding.run {
                closeButton.setOnClickListener {
                    dialog.dismiss()
                    findNavController().popBackStack(R.id.changePinFragment, true)
                }
            }
        }
    }
}