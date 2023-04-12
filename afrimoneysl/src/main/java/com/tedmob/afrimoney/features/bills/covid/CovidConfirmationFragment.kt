package com.tedmob.afrimoney.features.bills.covid

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
class CovidConfirmationFragment :
    BaseVBFragment<FragmentCovidConfirmationBinding>() {

    private val viewModel by provideNavGraphViewModel<CovidViewModel>(R.id.nav_covid)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentCovidConfirmationBinding::inflate)
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


            cancelButton.setDebouncedOnClickListener { viewModel.getCancelResult(pinInputLayout.getText())  }

            confirmButton.setDebouncedOnClickListener { viewModel.getConfirmResult(pinInputLayout.getText()) }
        }

    }

    private fun FragmentCovidConfirmationBinding.observeData() {
        withVBAvailable {
            observeResource(viewModel.data) {

                setupData(it)

            }
            observeTransactionSubmit(viewModel.submitted, confirmButton) {
                exit()
            }
        }
    }


    private fun FragmentCovidConfirmationBinding.setupData(data: CovidViewModel.CovidData) {
        with(data) {
            travelId.text = data.number
            amountText.text = getString(R.string.amount_currency, data.amount)
        }
    }




    private inline fun exit() {
        findNavController().popBackStack(R.id.payMyBillsFragment, false)
    }
}