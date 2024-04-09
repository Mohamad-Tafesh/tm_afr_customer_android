package com.tedmob.afrimoney.features.bills.nawec

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.data.api.dto.ClientDetails
import com.tedmob.afrimoney.databinding.*
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.viewmodel.observeResourceFromButton
import com.tedmob.afrimoney.ui.viewmodel.observeResourceInline
import com.tedmob.afrimoney.ui.viewmodel.provideNavGraphViewModel
import com.tedmob.afrimoney.util.getText
import com.tedmob.afrimoney.util.phone.BaseVBFragmentWithImportContact
import com.tedmob.libraries.validators.FormValidator
import com.tedmob.libraries.validators.formValidator
import com.tedmob.libraries.validators.rules.NotEmptyRule
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class BuyNawecFragment : BaseVBFragmentWithImportContact<FragmentBuyNawecBinding>() {

    private val viewModel by provideNavGraphViewModel<NawecViewModel>(R.id.nav_nawec)

    private var validator: FormValidator? = null

    var meterList = mutableListOf<ClientDetails>()
    var meter: NawecMeterData? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentBuyNawecBinding::inflate, true)
    }

    override fun configureToolbar() {
        actionbar?.show()
        actionbar?.title = ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding?.meterNumber?.setOnClickListener {
            findNavController().navigate(
                BuyNawecFragmentDirections.actionBuyNawecFragmentToNawecMetersBottomSheetFragment(
                    meterList.toTypedArray()
                )
            )
        }


        observeResourceInline(viewModel.clients) {

            meterList = it.list.toMutableList()
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<NawecMeterData>("data")
            ?.observe(
                viewLifecycleOwner
            ) { data ->
                binding?.meterNumber?.text = data.meterNumber
                meter = NawecMeterData(data.meterNumber, data.nickname)

            }



        observeResourceFromButton(viewModel.proceedToConfirm, R.id.proceedButton) {
            proceed()
        }



        withVBAvailable {


            newRadioBtn.isChecked = true
            validator = setupValidation()

            radioGroup.setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.newRadioBtn -> {
                        meterNumberInput.isVisible = true
                        meterNumber.isVisible = false
                        validator?.stop()
                        validator = setupValidation()
                    }

                    R.id.savedRadioBtn -> {
                        meterNumberInput.isVisible = false
                        meterNumber.isVisible = true
                        validator?.stop()
                        validator = setupValidation()
                    }
                }

            }

            proceedButton.setDebouncedOnClickListener {

                if (newRadioBtn.isChecked) {

                    validator?.submit(viewLifecycleOwner.lifecycleScope)
                } else {
                    if (meter != null) {

                        validator?.submit(viewLifecycleOwner.lifecycleScope)

                    } else {
                        showMessage(resources.getString(R.string.please_select_your_client_id_first))
                    }
                }


            }

            viewModel.getClients()

        }


    }

    private fun FragmentBuyNawecBinding.setupValidation(): FormValidator = formValidator {
        val notEmptyRule = NotEmptyRule(getString(R.string.mandatory_field))

        if (newRadioBtn.isChecked) {
            meterNumberInput.validate(notEmptyRule)
            amount.validate(notEmptyRule)
            onValid = {
                viewModel.getBuyFees(
                    meterNumberInput.getText(),
                    amount.getText()
                )
            }
        } else {
            amount.validate(notEmptyRule)
            onValid = {


                viewModel.getBuyFees(
                    meterNumber.text.toString(),
                    amount.getText()
                )


            }
        }


    }


    private fun proceed() {

        findNavController().navigate(BuyNawecFragmentDirections.actionBuyNawecFragmentToBuyNawecConfirmationFragment())

    }


}