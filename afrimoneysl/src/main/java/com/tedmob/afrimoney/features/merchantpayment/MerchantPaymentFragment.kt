package com.tedmob.afrimoney.features.merchantpayment

import android.os.Bundle
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
import com.tedmob.afrimoney.databinding.FragmentMerchantPaymentBinding
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
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MerchantPaymentFragment : BaseVBFragmentWithImportContact<FragmentMerchantPaymentBinding>() {

    private val viewModel by provideNavGraphViewModel<MerchantPaymentViewModel>(R.id.nav_merchant_payment)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentMerchantPaymentBinding::inflate)
    }

    override fun configureToolbar() {
        actionbar?.show()
        actionbar?.title = ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        debugOnly {
            binding!!.merchantcode.setText("59301")
        }

        observeResource(viewModel.proceedToConfirm) {
            proceed()

        }

        bindData()

        withVBAvailable {
            val validator = setupValidation()
            proceedButton.setDebouncedOnClickListener { validator.submit(viewLifecycleOwner.lifecycleScope) }
            barcodeScan.setDebouncedOnClickListener { scanBarcode() }

        }

    }


    private fun FragmentMerchantPaymentBinding.setupValidation(): FormValidator = formValidator {
        val notEmptyRule = NotEmptyRule(getString(R.string.mandatory_field))

        merchantcode.validate(
            notEmptyRule,
        )


        amountInput.validate(
            notEmptyRule,
            DoubleRule(getString(R.string.invalid_amount))
        )



        onValid = {

            if ((amountInput.getText().toDoubleOrNull()?:0.0)>0){
                viewModel.proceed(
                    merchantcode.getText(),
                    amountInput.getText().toDoubleOrNull() ?: 0.0,
                    binding!!.referenceId.getText().takeIf { it.isNotBlank() } ?: ""

                )

            }else{
                showMessage("Please enter your amount")
            }


        }
    }


    private fun proceed() {

        viewModel.getFees(binding!!.merchantcode.getText(), binding!!.amountInput.getText())

    }

    private fun bindData() {
        observeResourceFromButton(viewModel.data, R.id.proceedButton) {
            findNavController().navigate(
                MerchantPaymentFragmentDirections.actionMerchantPaymentFragmentToMerchantPaymentConfirmationFragment()
            )
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
            requireBinding().merchantcode.setText(result.contents.toString())
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

}