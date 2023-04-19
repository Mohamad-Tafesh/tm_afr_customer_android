package com.tedmob.afrimoney.features.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.debugOnly
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.databinding.FragmentSetPinBinding
import com.tedmob.afrimoney.ui.blocks.showLoading
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.viewmodel.observeResourceFromButton
import com.tedmob.afrimoney.ui.viewmodel.observeResourceInline
import com.tedmob.afrimoney.ui.viewmodel.provideViewModel
import com.tedmob.afrimoney.util.getText
import com.tedmob.afrimoney.util.setText
import com.tedmob.libraries.validators.formValidator
import com.tedmob.libraries.validators.rules.NotEmptyRule
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class SetPinFragment : BaseVBFragment<FragmentSetPinBinding>() {

    @Inject
    lateinit var appSessionNavigator: AppSessionNavigator

    private val viewModel by provideViewModel<SetPinViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(container, FragmentSetPinBinding::inflate)
    }


    override fun configureToolbar() {
        actionbar?.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewLifecycleOwner.lifecycleScope.launch {
            withVBAvailable {
                logoutButton.text = resources.getText(R.string.back)
                barcodeLayout.showLoading()
                barcodeLayout.showContent()
                setupBarcode(session.msisdn)

            }

            debugOnly {//4827 077928946
                binding?.pinInputLayout?.setText(R.string.debug_password)
            }
        }


        withVBAvailable {
            val validator = setupValidation()
            proceedButton.setDebouncedOnClickListener { validator.submit(viewLifecycleOwner.lifecycleScope) }
            logoutButton.setDebouncedOnClickListener {
                activity?.finish()

            }


        }

        observeResourceFromButton(viewModel.pinEntered, R.id.proceedButton) {
            proceedWith()
        }

    }

    private fun FragmentSetPinBinding.setupValidation() = formValidator {
        pinInputLayout.validate(NotEmptyRule(getString(R.string.mandatory_field)))

        onValid = {
            viewModel.enterPin(session.msisdn,pinInputLayout.getText())
           // viewModel.enterPin("090227946", pinInputLayout.getText())
        }
    }

    private fun proceedWith() {
        findNavController().navigate(SetPinFragmentDirections.actionSetPinFragment2ToHomeFragment())


    }

    private fun FragmentSetPinBinding.setupBarcode(userId: String) {
        val code = userId
        try {
            val requiredWidth =
                (root.context.resources.displayMetrics.widthPixels * 0.75).roundToInt()
            //TODO get dimensions from barcodeImage
            val bitmap = BarcodeEncoder().encodeBitmap(code, BarcodeFormat.QR_CODE, requiredWidth, requiredWidth)
            barcodeImage.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}