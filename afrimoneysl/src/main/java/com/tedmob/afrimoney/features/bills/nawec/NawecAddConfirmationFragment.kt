package com.tedmob.afrimoney.features.bills.nawec

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
class NawecAddConfirmationFragment :
    BaseVBFragment<FragmentNawecAddConfirmationBinding>() {

    private val viewModel by provideNavGraphViewModel<NawecViewModel>(R.id.nav_nawec)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentNawecAddConfirmationBinding::inflate)
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

    private fun FragmentNawecAddConfirmationBinding.observeData() {
        withVBAvailable {

            setupData()

            observeResource(viewModel.submitted) {
                if (it.biller?.status == "200") {
                    showTransactionSuccess(
                        it.biller?.message!!,
                        resources.getString(R.string.successful)
                    ) { exit() }
                } else {
                    showTransactionFailure(
                        it.biller?.message!!,
                        resources.getString(R.string.failed)
                    ) { exit() }
                }
            }
        }
    }


    private fun FragmentNawecAddConfirmationBinding.setupData() {
        binding?.meterNumber?.text = viewModel.meterId
        binding?.nicknameText?.text = viewModel.nickname

    }

    private inline fun FragmentNawecAddConfirmationBinding.setupValidator() = formValidator {
        pinInputLayout.validate(NotEmptyRule(getString(R.string.mandatory_field)))

        onValid = {

            viewModel.getConfirmationAdd(pinInputLayout.getText())
        }
    }


    private inline fun exit() {
        findNavController().popBackStack(R.id.payMyBillsFragment, false)
    }
}