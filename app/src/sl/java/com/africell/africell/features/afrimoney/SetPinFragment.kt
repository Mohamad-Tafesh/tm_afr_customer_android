package com.africell.africell.features.afrimoney

import android.os.Bundle
import android.view.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.debugOnly
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.data.entity.UserState
import com.tedmob.afrimoney.databinding.FragmentEnterPinBinding
import com.tedmob.afrimoney.databinding.FragmentLoginBinding
import com.tedmob.afrimoney.databinding.FragmentSetPinBinding
import com.tedmob.afrimoney.exception.AppException
import com.tedmob.afrimoney.ui.blocks.showLoading
import com.tedmob.afrimoney.ui.blocks.showMessage
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.viewmodel.observeResourceFromButton
import com.tedmob.afrimoney.ui.viewmodel.observeResourceProgress
import com.tedmob.afrimoney.ui.viewmodel.provideViewModel
import com.tedmob.afrimoney.util.getText
import com.tedmob.afrimoney.util.setText
import com.tedmob.afrimoney.util.suspendForOneSignalUserId
import com.tedmob.libraries.validators.formValidator
import com.tedmob.libraries.validators.rules.NotEmptyRule

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class SetPinFragment : BaseVBFragment<FragmentSetPinBinding>() {

    @Inject
    lateinit var appSessionNavigator: AppSessionNavigator

    private val viewModel by provideViewModel<SetPinViewModel>()
    private val args by navArgs<SetPinFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(container, FragmentSetPinBinding::inflate)
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
                setupBarcode(args.mobilenb.orEmpty())

            }

            debugOnly {//4827 077928946
            binding?.pinInputLayout?.setText(R.string.debug_password)
            }
        }


        withVBAvailable {
          val validator = setupValidation()
     proceedButton.setDebouncedOnClickListener { validator.submit(viewLifecycleOwner.lifecycleScope)  }
            logoutButton.setDebouncedOnClickListener { viewModel.logout() }


        }

        observeResourceFromButton(viewModel.pinEntered, R.id.proceedButton) {
            unblockApp()
            proceedWith()
        }

    }

    private fun FragmentSetPinBinding.setupValidation() = formValidator {
        pinInputLayout.validate(NotEmptyRule(getString(R.string.mandatory_field)))

        onValid = {
            //viewModel.enterPin(args.mobilenb.orEmpty(),pinInputLayout.getText())
            viewModel.enterPin("090227946",pinInputLayout.getText())
        }
    }
    private inline fun proceedWith() {
     //   findNavController().navigate(SetPinFragmentDirections.actionSetPinFragmentToMainActivity(false))
       // findNavController().navigate(R.id.afrimoneyFragment)

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

    private fun bindLoggedOut() {
        observeResourceProgress(viewModel.loggedOut) {
            appSessionNavigator.restart()
        }
    }

    private fun unblockApp() {
        activity?.finish()
    }
}