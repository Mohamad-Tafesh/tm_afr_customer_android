package com.tedmob.afrimoney.features.withdraw.agentcode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.debugOnly
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.databinding.FragmentAgentCodeBinding
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.spinner.MaterialSpinner
import com.tedmob.afrimoney.ui.spinner.OnItemSelectedListener
import com.tedmob.afrimoney.ui.viewmodel.observeResourceFromButton
import com.tedmob.afrimoney.ui.viewmodel.provideNavGraphViewModel
import com.tedmob.afrimoney.util.getText
import com.tedmob.afrimoney.util.setText
import com.tedmob.libraries.validators.FormValidator
import com.tedmob.libraries.validators.formValidator
import com.tedmob.libraries.validators.rules.NotEmptyRule
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AgentCodeFragment : BaseVBFragment<FragmentAgentCodeBinding>() {

    private val viewModel by provideNavGraphViewModel<AgentCodeViewModel>(R.id.nav_withdraw)

    var wallet: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentAgentCodeBinding::inflate)
    }

    override fun configureToolbar() {
        actionbar?.show()
        actionbar?.title = ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        debugOnly {
            withVBAvailable {
                agentCode.setText("34285")

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


    private fun FragmentAgentCodeBinding.setupValidation(): FormValidator = formValidator {
        val notEmptyRule = NotEmptyRule(getString(R.string.mandatory_field))

        agentCode.validate(
            notEmptyRule,
        )

        amount.validate(
            notEmptyRule,
        )

        walletInput.validate(
            notEmptyRule,
        )

        onValid = {
            viewModel.getFees(binding!!.agentCode.getText(), binding!!.amount.getText(),wallet!!)

        }
    }


    private fun bindData() {
        observeResourceFromButton(viewModel.data, R.id.proceedButton) {

            findNavController().navigate(AgentCodeFragmentDirections.actionAgentcodeToAgentCodeConfirmationFragment())

        }
    }


}