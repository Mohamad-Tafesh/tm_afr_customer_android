package com.africell.africell.features.afrimoney

import android.os.Bundle
import android.view.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.africell.africell.R
import com.africell.africell.app.AppSessionNavigator
import com.africell.africell.app.debugOnly
import com.africell.africell.app.viewbinding.BaseVBFragment
import com.africell.africell.app.viewbinding.withVBAvailable
import com.africell.africell.databinding.FragmentSetPinAfricellBinding
import com.africell.africell.ui.blocks.showLoading
import com.africell.africell.ui.button.setDebouncedOnClickListener
import com.africell.africell.ui.viewmodel.observeResourceFromButton
import com.africell.africell.ui.viewmodel.observeResourceInline
import com.africell.africell.ui.viewmodel.provideViewModel
import com.africell.africell.util.getText
import com.africell.africell.util.setText
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.tedmob.afrimoney.databinding.FragmentSetPinBinding
import com.tedmob.libraries.validators.formValidator
import com.tedmob.libraries.validators.rules.NotEmptyRule
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class SetPinFragment : BaseVBFragment<FragmentSetPinAfricellBinding>() {

    @Inject
    lateinit var appSessionNavigator: AppSessionNavigator

    private val viewModel by provideViewModel<SetPinViewModel>()
    private val args by navArgs<SetPinFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(container, FragmentSetPinAfricellBinding::inflate)
    }


    override fun configureToolbar() {
        actionbar?.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindLoggedOut()

        viewLifecycleOwner.lifecycleScope.launch {
            withVBAvailable {
                barcodeLayout.showLoading()
                barcodeLayout.showContent()
                //setupBarcode(args.mobilenb.orEmpty())

            }

            debugOnly {//4827 077928946
                binding?.pinInputLayout?.setText(R.string.debug_password)
            }
        }


        withVBAvailable {
            val validator = setupValidation()
            proceedButton.setDebouncedOnClickListener { validator.submit(viewLifecycleOwner.lifecycleScope) }
            logoutButton.setDebouncedOnClickListener { viewModel.logout() }


        }

        observeResourceFromButton(viewModel.pinEntered, R.id.proceedButton) {
            unblockApp()
            proceedWith()
        }

    }

    private fun FragmentSetPinAfricellBinding.setupValidation() = formValidator {
        pinInputLayout.validate(NotEmptyRule(getString(R.string.mandatory_field)))

        onValid = {
            //viewModel.enterPin(args.mobilenb.orEmpty(),pinInputLayout.getText())
            viewModel.enterPin("090227946", pinInputLayout.getText())
        }
    }

    private inline fun proceedWith() {
        //   findNavController().navigate(SetPinFragmentDirections.actionSetPinFragmentToMainActivity(false))
        findNavController().navigate(R.id.afrimoneyFragment)

    }

    private fun FragmentSetPinAfricellBinding.setupBarcode(userId: String) {
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

    private fun bindLoggedOut() {
        observeResourceInline(viewModel.loggedOut) {
            appSessionNavigator.restart()
        }
    }

    private fun unblockApp() {
       // activity?.finish()
    }
}