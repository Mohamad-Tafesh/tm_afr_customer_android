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
import androidx.core.view.get
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
import com.tedmob.afrimoney.data.entity.GetFeesData
import com.tedmob.afrimoney.databinding.FragmentTransferMoneyBinding
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.spinner.MaterialSpinner
import com.tedmob.afrimoney.ui.spinner.OnItemSelectedListener
import com.tedmob.afrimoney.ui.viewmodel.observeResource
import com.tedmob.afrimoney.ui.viewmodel.observeResourceFromButton
import com.tedmob.afrimoney.ui.viewmodel.provideNavGraphViewModel
import com.tedmob.afrimoney.util.getText
import com.tedmob.afrimoney.util.phone.BaseVBFragmentWithImportContact
import com.tedmob.afrimoney.util.phone.PhoneNumber2Helper
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


    private var type = 0

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


            /* typesInput.adapter = ArrayAdapter(
                 requireContext(),
                 R.layout.support_simple_spinner_dropdown_item,
                 listOf("Send to others", "Remittance to Normal Wallet")
             )
 */

            radioGroup.setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {

                    R.id.toOther -> {
                        type = 0
                        othersGrp.isVisible = true
                        title.text = getString(R.string.send_to_other)
                        validator.stop()
                        validator = setupValidation()
                    }

                    R.id.remittance -> {
                        type = 1
                        othersGrp.isVisible = false
                        title.text = getString(R.string.remittance_to_normal_wallet)
                        validator.stop()
                        validator = setupValidation()
                    }

                }

            }


        }
    }

    private fun FragmentTransferMoneyBinding.setupValidation(): FormValidator = formValidator {
        val notEmptyRule = NotEmptyRule(getString(R.string.mandatory_field))

        if (othersGrp.isVisible) {
            /*            validatePhoneFields(
                            countryCode.getValidationField(notEmptyRule),
                            mobileNumberInput.getValidationField(notEmptyRule),
                            PhoneRule(
                                getString(R.string.invalid_mobile_number),
                                phoneUtil,
                                type = PhoneRule.Type.MOBILE_OR_UNKNOWN
                            )
                        )*/

            mobileNumberInput.validate(
                notEmptyRule,
            )

            amountInput.validate(
                notEmptyRule,
                DoubleRule(getString(R.string.invalid_amount))
            )

        } else {

            amountInput.validate(
                notEmptyRule,
                DoubleRule(getString(R.string.invalid_amount))
            )
        }



        onValid = {

            if ((amountInput.getText().toDoubleOrNull() ?: 0.0) > 0) {

                var number = mobileNumberInput.getText()

                if (mobileNumberInput.getText().length == 8) number = "0" + mobileNumberInput.getText()

                if ((type ?: 0) == 1) {
                    viewModel.amount = amountInput.getText()
                    viewModel.feesData = GetFeesData("", amountInput.getText(), "", null, "")
                    findNavController().navigate(
                        TransferMoneyFragmentDirections.actionTransferMoneyFragmentToTransferMoneyConfirmationFragment()
                    )
                } else {

                    withVBAvailable {
                        val phoneCode = countryCode.getText()

                        val formatted = if (number.length == 9 && (
                                    number.startsWith("1") ||
                                            number.startsWith("2") ||
                                            number.startsWith("3") ||
                                            number.startsWith("4") ||
                                            number.startsWith("5") ||
                                            number.startsWith("6") ||
                                            number.startsWith("7") ||
                                            number.startsWith("8") ||
                                            number.startsWith("9")
                                    )
                        ) {
                            "Done"
                        } else null

                        formatted?.let {
                            viewModel.proceed(number, amountInput.getText().toDoubleOrNull() ?: 0.0)
                        } ?: showMessage(getString(R.string.invalid_mobile_number))
                    }

                }


            } else {
                showMessage("Please enter your amount")
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
        viewModel.getFees(viewModel.number, binding!!.amountInput.getText(), type!!)

    }

    private fun bindData() {
        observeResource(viewModel.data) { data ->
            findNavController().navigate(
                TransferMoneyFragmentDirections.actionTransferMoneyFragmentToTransferMoneyConfirmationFragment()
            )
        }
    }

}