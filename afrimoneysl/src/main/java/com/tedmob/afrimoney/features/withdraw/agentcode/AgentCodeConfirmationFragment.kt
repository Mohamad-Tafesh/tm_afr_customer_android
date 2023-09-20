package com.tedmob.afrimoney.features.withdraw.agentcode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.debugOnly
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.databinding.*
import com.tedmob.afrimoney.data.entity.GetFeesData
import com.tedmob.afrimoney.ui.applySheetEffectTo
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.viewmodel.observeResourceInline
import com.tedmob.afrimoney.ui.viewmodel.observeTransactionSubmit
import com.tedmob.afrimoney.ui.viewmodel.provideNavGraphViewModel
import com.tedmob.afrimoney.util.getText
import com.tedmob.afrimoney.util.setText
import com.tedmob.libraries.validators.formValidator
import com.tedmob.libraries.validators.rules.NotEmptyRule
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AgentCodeConfirmationFragment :
    BaseVBFragment<FragmentAgentCodeConfirmationBinding>() {

    private val viewModel by provideNavGraphViewModel<AgentCodeViewModel>(R.id.nav_withdraw)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentAgentCodeConfirmationBinding::inflate)
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

            debugOnly { pinInputLayout.setText(R.string.debug_password) }
        }

    }

    private fun FragmentAgentCodeConfirmationBinding.observeData() {

        observeResourceInline(viewModel.data,contentLL){
            setupData(it)
        }

       observeTransactionSubmit(viewModel.submitted, confirmButton) {
           exit()
       }
    }


    private fun FragmentAgentCodeConfirmationBinding.setupData(data: GetFeesData) {
        with(data) {
            amountText.text = getString(R.string.amount_currency,amount)
            agentCodeText.text = number
            totalText.text =  getString(R.string.amount_currency,total)
            feesText.text = getString(R.string.fees_new, data.fees)
            nameText.text =name

        }
    }

    private inline fun FragmentAgentCodeConfirmationBinding.setupValidator() = formValidator {
        pinInputLayout.validate(NotEmptyRule(getString(R.string.mandatory_field)))

        onValid = {
            viewModel.getConfirmation(agentCodeText.text.toString(),pinInputLayout.getText())
        }
    }


    private inline fun exit() {
        findNavController().popBackStack(R.id.withdrawMain, false)
    }
}