package com.tedmob.afrimoney.features.bills.dstv

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
import com.tedmob.afrimoney.databinding.FragmentCheckDstvBinding
import com.tedmob.afrimoney.databinding.FragmentCheckDstvConfirmationBinding
import com.tedmob.afrimoney.databinding.FragmentMerchantPaymentConfirmationBinding
import com.tedmob.afrimoney.databinding.FragmentRenewDstvConfirmationBinding
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
class CheckDSTVConfirmationFragment :
    BaseVBFragment<FragmentCheckDstvConfirmationBinding>() {

    private val viewModel by provideNavGraphViewModel<CheckDSTVViewModel>(R.id.nav_dstv)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentCheckDstvConfirmationBinding::inflate)
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

            confirmButton.setDebouncedOnClickListener { viewModel.getConfirmation(smartCardText.text.toString()) }


            observeTransactionSubmit(viewModel.submitted, confirmButton) {
                exit()
            }
        }

    }

    private fun FragmentCheckDstvConfirmationBinding.observeData() {
        observeResource(viewModel.data) {
            setupData(it)
        }

    }


    private fun FragmentCheckDstvConfirmationBinding.setupData(data: String) {
        smartCardText.text = data
    }


    private inline fun exit() {
        findNavController().popBackStack(R.id.payMyBillsFragment, false)
    }
}