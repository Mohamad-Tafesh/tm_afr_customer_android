package com.tedmob.afrimoney.features.transfer

import android.content.Context
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.zxing.BarcodeFormat
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.debugOnly
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.data.entity.Country
import com.tedmob.afrimoney.databinding.FragmentTransferMoneyBinding
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
import com.tedmob.libraries.validators.rules.DoubleRule
import com.tedmob.libraries.validators.rules.NotEmptyRule
import com.tedmob.libraries.validators.rules.phone.PhoneRule
import dagger.hilt.android.AndroidEntryPoint
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class TransferMoneyFragment : BaseVBFragmentWithImportContact<FragmentTransferMoneyBinding>() {

    private val viewModel by provideNavGraphViewModel<TransferMoneyViewModel>(R.id.nav_transfer_money)

    @Inject
    internal lateinit var phoneUtil: PhoneNumberUtil

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentTransferMoneyBinding::inflate)
    }

    override fun configureToolbar() {
        actionbar?.show()
        actionbar?.title = ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        bindData()

        observeResourceFromButton(viewModel.proceedToConfirm, R.id.proceedButton) {
            proceed()

        }
        debugOnly {
            binding?.mobileNumberInput?.setText("090227948")
        }

        withVBAvailable {

            mobileNumberInput.setEndIconOnClickListener {
                runImportContactFlow()
            }

            var validator = setupValidation()
            proceedButton.setDebouncedOnClickListener { validator.submit(viewLifecycleOwner.lifecycleScope) }
            barcodeScan.setOnClickListener() {
                scanBarcode()
            }


            typesInput.adapter = ArrayAdapter(
                requireContext(),
                R.layout.support_simple_spinner_dropdown_item,
                listOf("Send to others", "Remittance Account")
            )


            typesInput.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: MaterialSpinner,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position == 0) {
                        othersGrp.isVisible=true
                        validator.stop()
                        validator = setupValidation()
                    } else if (position == 1) {
                        othersGrp.isVisible=false
                        validator.stop()
                        validator = setupValidation()
                    }
                }

                override fun onNothingSelected(parent: MaterialSpinner) {

                }
            }


        }
    }

    private fun FragmentTransferMoneyBinding.setupValidation(): FormValidator = formValidator {
        val notEmptyRule = NotEmptyRule(getString(R.string.mandatory_field))

        if (othersGrp.isVisible){
            validatePhoneFields(
                countryCode.getValidationField(notEmptyRule),
                mobileNumberInput.getValidationField(notEmptyRule),
                PhoneRule(
                    getString(R.string.invalid_mobile_number),
                    phoneUtil,
                    type = PhoneRule.Type.MOBILE_OR_UNKNOWN
                )
            )

            typesInput.validate(
                notEmptyRule,
            )

            amountInput.validate(
                notEmptyRule,
                DoubleRule(getString(R.string.invalid_amount))
            )

        }else{

            typesInput.validate(
                notEmptyRule,
            )

            amountInput.validate(
                notEmptyRule,
                DoubleRule(getString(R.string.invalid_amount))
            )
        }



        onValid = {

            var number = mobileNumberInput.getText()
            if (mobileNumberInput.getText().length == 8) number = "0" + mobileNumberInput.getText()


            if (othersGrp.isVisible){
                viewModel.proceed(
                    number,
                    amountInput.getText().toDoubleOrNull() ?: 0.0,
                )
            }else{
                viewModel.proceed(
                    number,
                    amountInput.getText().toDoubleOrNull() ?: 0.0,
                )
            }


        }
    }


    private inline fun runImportContactFlow() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            importContact(Country.DEFAULT_CODE)?.let { binding?.mobileNumberInput?.setText(it) }
        }
    }

    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            val originalIntent = result.originalIntent
            if (originalIntent == null) {
                Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
            } else if (originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                Toast.makeText(
                    context,
                    "Cancelled due to missing camera permission",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            requireBinding().mobileNumberInput.setText(result.contents.toString())
        }
    }


    private fun scanBarcode() {
        barcodeLauncher.launch(
            ScanOptions()
                .setPrompt("Scan")
                .setOrientationLocked(true)
                .setDesiredBarcodeFormats(BarcodeFormat.QR_CODE.name)
        )
    }

    private fun proceed() {

        viewModel.getFees(viewModel.number, binding!!.amountInput.getText())

    }

    private fun bindData() {
        observeResource(viewModel.data) { data ->
            findNavController().navigate(
                TransferMoneyFragmentDirections.actionTransferMoneyFragmentToTransferMoneyConfirmationFragment()
            )
        }
    }

}