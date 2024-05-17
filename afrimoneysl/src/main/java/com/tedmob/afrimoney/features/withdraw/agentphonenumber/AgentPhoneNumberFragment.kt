package com.tedmob.afrimoney.features.withdraw.agentphonenumber

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.debugOnly
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.databinding.FragmentAgentCodeBinding
import com.tedmob.afrimoney.databinding.FragmentAgentPhoneNumberBinding
import com.tedmob.afrimoney.features.withdraw.agentcode.AgentCodeFragmentDirections
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.spinner.MaterialSpinner
import com.tedmob.afrimoney.ui.spinner.OnItemSelectedListener
import com.tedmob.afrimoney.ui.viewmodel.observeResource
import com.tedmob.afrimoney.ui.viewmodel.observeResourceFromButton
import com.tedmob.afrimoney.ui.viewmodel.provideNavGraphViewModel
import com.tedmob.afrimoney.util.getText
import com.tedmob.afrimoney.util.phone.BaseVBFragmentWithImportContact
import com.tedmob.afrimoney.util.phone.PhoneNumberHelper
import com.tedmob.afrimoney.util.setText
import com.tedmob.libraries.validators.FormValidator
import com.tedmob.libraries.validators.formValidator
import com.tedmob.libraries.validators.rules.NotEmptyRule
import com.tedmob.libraries.validators.rules.phone.PhoneRule
import dagger.hilt.android.AndroidEntryPoint
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import javax.inject.Inject


@AndroidEntryPoint
class AgentPhoneNumberFragment :
    BaseVBFragmentWithImportContact<FragmentAgentPhoneNumberBinding>() {

    private val viewModel by provideNavGraphViewModel<AgentPhoneNumberViewModel>(R.id.nav_withdraw)

    var wallet: String? = null


    @Inject
    internal lateinit var phoneUtil: PhoneNumberUtil

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentAgentPhoneNumberBinding::inflate)
    }

    override fun configureToolbar() {
        actionbar?.show()
        actionbar?.title = ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        debugOnly {
            withVBAvailable {
                agentNumber.setText("077777737")

            }
        }

        withVBAvailable {

            bindData()

            val validator = setupValidation()


            proceedButton.setDebouncedOnClickListener { validator.submit(viewLifecycleOwner.lifecycleScope) }

            walletInput.adapter = ArrayAdapter(
                requireContext(),
                R.layout.support_simple_spinner_dropdown_item,
                listOf("Normal","Remittance")
            )


            walletInput.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: MaterialSpinner,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    wallet = when (position) {
                        0 -> "Normal"
                        1 -> "Bonus"
                        2 -> "Remittance"
                        else -> {
                            null
                        }
                    }

                }

                override fun onNothingSelected(parent: MaterialSpinner) {

                }
            }

        }


    }


    private fun FragmentAgentPhoneNumberBinding.setupValidation(): FormValidator = formValidator {
        val notEmptyRule = NotEmptyRule(getString(R.string.mandatory_field))


        validatePhoneFields(
            countryCode.getValidationField(notEmptyRule),
            agentNumber.getValidationField(notEmptyRule),
            PhoneRule(
                getString(R.string.invalid_mobile_number),
                phoneUtil,
                type = PhoneRule.Type.MOBILE_OR_UNKNOWN
            )
        )

        amount.validate(
            notEmptyRule,
        )

        walletInput.validate(
            notEmptyRule,
        )


        onValid = {
            var number = agentNumber.getText()
            if (agentNumber.getText().length == 8) number = "0" + agentNumber.getText()

            viewModel.getFees(number, amount.getText(),wallet!!)


        }
    }

    private fun bindData() {
        observeResourceFromButton(viewModel.data, R.id.proceedButton) {

            findNavController().navigate(AgentPhoneNumberFragmentDirections.actionAgentPhoneNumberFragmentToAgentPhoneNumberConfirmationFragment())

        }
    }


}