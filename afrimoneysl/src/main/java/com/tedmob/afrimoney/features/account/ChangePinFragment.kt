package com.tedmob.afrimoney.features.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.databinding.FragmentChangePinBinding
import com.tedmob.afrimoney.ui.button.observeInView
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.hideKeyboard
import com.tedmob.afrimoney.ui.viewmodel.provideNavGraphViewModel
import com.tedmob.afrimoney.util.getText
import com.tedmob.afrimoney.util.navigation.runIfFrom
import com.tedmob.libraries.validators.formValidator
import com.tedmob.libraries.validators.rules.NotEmptyRule
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePinFragment : BaseVBFragment<FragmentChangePinBinding>() {

    private val viewModel by provideNavGraphViewModel<ChangePinViewModel>(R.id.nav_change_pin)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(container, FragmentChangePinBinding::inflate, false)
    }

    override fun configureToolbar() {
  actionbar?.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        withVBAvailable {
            val validator = setupValidator()
            proceedButton.setDebouncedOnClickListener {
                hideKeyboard()
                validator.submit(viewLifecycleOwner.lifecycleScope)
            }
        }
    }

    private fun FragmentChangePinBinding.setupValidator() = formValidator {
        val notEmptyRule = NotEmptyRule(getString(R.string.mandatory_field))

        oldPin.validate(notEmptyRule)

        onValid = { proceed() }
    }




    private fun proceed() {
        viewModel.oldpin=requireBinding().oldPin.getText()
        findNavController().runIfFrom(R.id.changePinFragment) {
            navigate(ChangePinFragmentDirections.actionChangePinFragmentToChangePinNewPinFragment())
        }
    }
}