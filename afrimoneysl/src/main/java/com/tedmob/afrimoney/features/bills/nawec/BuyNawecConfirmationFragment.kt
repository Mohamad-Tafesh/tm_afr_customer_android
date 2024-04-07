package com.tedmob.afrimoney.features.bills.nawec

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.databinding.FragmentBuyNawecConfirmationBinding
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.viewmodel.observeTransactionSubmit
import com.tedmob.afrimoney.ui.viewmodel.provideNavGraphViewModel
import com.tedmob.afrimoney.util.getText
import com.tedmob.libraries.validators.formValidator
import com.tedmob.libraries.validators.rules.NotEmptyRule
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BuyNawecConfirmationFragment :
    BaseVBFragment<FragmentBuyNawecConfirmationBinding>() {

    private val viewModel by provideNavGraphViewModel<NawecViewModel>(R.id.nav_nawec)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentBuyNawecConfirmationBinding::inflate)
    }

    override fun configureToolbar() {
        actionbar?.show()
        actionbar?.title = ""

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        withVBAvailable {

            observeData()

            val validator = setupValidator()
            confirmButton.setDebouncedOnClickListener { validator.submit(viewLifecycleOwner.lifecycleScope) }
        }

    }

    private fun FragmentBuyNawecConfirmationBinding.observeData() {
        withVBAvailable {

            setupData(viewModel.prepaidFeesData!!)


            /*            observeResource(viewModel.submitted) {
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
                        }*/

            observeTransactionSubmit(viewModel.confirmEndePrePaidData, confirmButton) {
                exit()
            }
        }
    }


    private fun FragmentBuyNawecConfirmationBinding.setupData(data: GetNawecFeesData) {
        meterNumber.text = data.number
        name.text = data.name
        feesText.text = getString(R.string.fees_new, data.fees)
        payment.text = getString(R.string.amount_currency, data.amount)
        unit.text = "${data.unitValue}${data.siUnit}"
    }

    private inline fun FragmentBuyNawecConfirmationBinding.setupValidator() = formValidator {
        pinInputLayout.validate(NotEmptyRule(getString(R.string.mandatory_field)))

        onValid = {

            viewModel.getConfirmationBuyNawec(pinInputLayout.getText())
        }
    }


    private fun exit() {
        findNavController().popBackStack(R.id.payMyBillsFragment, false)
    }
}