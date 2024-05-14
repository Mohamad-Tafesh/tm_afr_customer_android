package com.tedmob.afrimoney.features.withdraw.agentcode

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
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.debugOnly
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.data.entity.IdTypeItem
import com.tedmob.afrimoney.databinding.FragmentAgentCodeBinding
import com.tedmob.afrimoney.features.transfer.TransferMoneyFragmentDirections
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.spinner.MaterialSpinner
import com.tedmob.afrimoney.ui.spinner.OnItemSelectedListener
import com.tedmob.afrimoney.ui.viewmodel.observeResource
import com.tedmob.afrimoney.ui.viewmodel.observeResourceFromButton
import com.tedmob.afrimoney.ui.viewmodel.provideNavGraphViewModel
import com.tedmob.afrimoney.util.getText
import com.tedmob.afrimoney.util.phone.BaseVBFragmentWithImportContact
import com.tedmob.afrimoney.util.setText
import com.tedmob.libraries.validators.FormValidator
import com.tedmob.libraries.validators.formValidator
import com.tedmob.libraries.validators.rules.NotEmptyRule
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class AgentCodeFragment : BaseVBFragment<FragmentAgentCodeBinding>() {

    private val viewModel by provideNavGraphViewModel<AgentCodeViewModel>(R.id.nav_withdraw)


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


        onValid = {
            viewModel.getFees(binding!!.agentCode.getText(), binding!!.amount.getText())

        }
    }


    private fun bindData() {
        observeResourceFromButton(viewModel.data, R.id.proceedButton) {

            findNavController().navigate(AgentCodeFragmentDirections.actionAgentcodeToAgentCodeConfirmationFragment())

        }
    }


}