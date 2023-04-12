package com.tedmob.afrimoney.features.transfer

import android.content.Context
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

            val validator = setupValidation()
            proceedButton.setDebouncedOnClickListener { validator.submit(viewLifecycleOwner.lifecycleScope) }
            barcodeScan.setOnClickListener() {
                scanBarcode()
            }
        }
    }

    private fun FragmentTransferMoneyBinding.setupValidation(): FormValidator = formValidator {
        val notEmptyRule = NotEmptyRule(getString(R.string.mandatory_field))

        validatePhoneFields(
            countryCode.getValidationField(notEmptyRule),
            mobileNumberInput.getValidationField(notEmptyRule),
            PhoneRule(
                getString(R.string.invalid_mobile_number),
                phoneUtil,
                type = PhoneRule.Type.MOBILE_OR_UNKNOWN
            )
        )


        amountInput.validate(
            notEmptyRule,
            DoubleRule(getString(R.string.invalid_amount))
        )


        onValid = {

            var number = mobileNumberInput.getText()
            if (mobileNumberInput.getText().length == 8) number = "0" + mobileNumberInput.getText()
            viewModel.proceed(
                number,
                amountInput.getText().toDoubleOrNull() ?: 0.0,
                )
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
        bindData()
    }

    private fun bindData() {
        observeResource(viewModel.data) { data ->
            findNavController().navigate(
                TransferMoneyFragmentDirections.actionTransferMoneyFragmentToTransferMoneyConfirmationFragment()
            )
        }
    }

}