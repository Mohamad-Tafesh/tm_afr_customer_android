package com.tedmob.afrimoney.features.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.debugOnly
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.data.entity.Country
import com.tedmob.afrimoney.databinding.FragmentLoginBinding
import com.tedmob.afrimoney.exception.AppException
import com.tedmob.afrimoney.ui.blocks.showLoading
import com.tedmob.afrimoney.ui.blocks.showMessage
import com.tedmob.afrimoney.ui.button.observeInView
import com.tedmob.afrimoney.ui.viewmodel.provideViewModel
import com.tedmob.afrimoney.util.getText
import com.tedmob.afrimoney.util.phone.PhoneNumberHelper
import com.tedmob.afrimoney.util.setText
import com.tedmob.afrimoney.util.suspendForOneSignalUserId
import com.tedmob.libraries.validators.FormValidator
import com.tedmob.libraries.validators.formValidator
import com.tedmob.libraries.validators.rules.NotEmptyRule
import com.tedmob.libraries.validators.rules.Rule
import com.tedmob.libraries.validators.rules.phone.PhoneRule
import dagger.hilt.android.AndroidEntryPoint
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class LoginFragment : BaseVBFragment<FragmentLoginBinding>() {

    @Inject
    lateinit var phoneHelper: PhoneNumberHelper

    private var validator: FormValidator? = null

    @Inject
    internal lateinit var phoneUtil: PhoneNumberUtil


    private val viewModel by provideViewModel<LoginViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentLoginBinding::inflate)
    }

    override fun configureToolbar() {
        actionbar?.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.setupFormValidation()
        binding?.loginButton?.setOnClickListener { validator?.submit(viewLifecycleOwner.lifecycleScope) }

        bindUser()



        debugOnly {//4827 077928946 //090227946 //077928946
            binding?.username?.setText(R.string.debug_number)
            validator?.validateFlow()?.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    private fun FragmentLoginBinding.setupFormValidation() {
        validator = formValidator {

            validatePhoneFields(
                countryCode.getValidationField(notEmptyRule),
                username.getValidationField(notEmptyRule),
                PhoneRule(
                    getString(R.string.invalid_mobile_number),
                    phoneUtil,
                    type = PhoneRule.Type.MOBILE_OR_UNKNOWN
                )
            )


            onValid = { performAction() }
        }
    }


    private fun bindUser() {
        viewModel.loginData.observeInView(this, requireBinding().loginButton) {
            onSuccess = {
                //fixme isClickable is set to true after hideProgress morph is finished, so we are blocking the button to prevent further clicks
                button?.isClickable = false
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToLoginVerificationFragment(
                        it.msisdn, it.otp
                    )


                )
            }
        }
    }


    private fun performAction() {
        withVBAvailable {
            var number = username.getText()
            if (username.getText().length == 8) number = "0" + username.getText()
            viewModel.login(number)
        }
    }
}
