package com.tedmob.afrimoney.features.authentication

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.debugOnly
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.data.entity.UserState
import com.tedmob.afrimoney.databinding.FragmentLoginVerificationBinding
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.hideKeyboard
import com.tedmob.afrimoney.ui.viewmodel.observeResourceFromButton
import com.tedmob.afrimoney.ui.viewmodel.observeResourceInline
import com.tedmob.afrimoney.ui.viewmodel.provideViewModel
import com.tedmob.afrimoney.util.getText
import com.tedmob.afrimoney.util.setText
import com.tedmob.libraries.validators.formValidator
import com.tedmob.libraries.validators.rules.NotEmptyRule
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginVerificationFragment : BaseVBFragment<FragmentLoginVerificationBinding>() {

    private val viewModel by provideViewModel<LoginViewModel>()
    private val args by navArgs<LoginVerificationFragmentArgs>()
    var counter: Int = 59
    lateinit var ct: CountDownTimer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentLoginVerificationBinding::inflate, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        bindUser()

        binding!!.timer.setOnClickListener() {

            viewModel.resend(args.mobilenb.orEmpty())
        }

        ct = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding!!.timer.setText("00:" + String.format("%02d", counter))
                counter--
            }

            override fun onFinish() {

                binding!!.timer.setText("Resend")
                binding!!.timer.isEnabled = true


            }
        }.start()

        val validator = formValidator {
            binding?.pinText?.validate(NotEmptyRule(getString(R.string.mandatory_field)))

            onValid = { verify() }
        }

        observeResourceFromButton(viewModel.verified, R.id.verifyButton) {
            proceedWith(it)
        }

        withVBAvailable {
            backButton.setDebouncedOnClickListener { findNavController().popBackStack() }
            verifyButton.setDebouncedOnClickListener {
                hideKeyboard()
                validator.submit(viewLifecycleOwner.lifecycleScope)
            }

            debugOnly {
                pinText.setText(args.pin.orEmpty())
            }
        }
    }

    private fun bindUser() {
        observeResourceInline(viewModel.resenddata) {

            binding!!.timer.isEnabled = false
            counter = 10
            ct.start()
            binding!!.pinText.setText(it.otp.orEmpty())

        }

    }

    private fun verify() {
        viewModel.verify(requireBinding().pinText.getText(), args.mobilenb.orEmpty())
    }

    private inline fun proceedWith(state: UserState) {
        when (state) {
            is UserState.NotRegistered -> {
                findNavController().navigate(
                    LoginVerificationFragmentDirections.actionLoginVerificationFragmentToNavRegister(
                        args.mobilenb.orEmpty()
                    )
                    //   LoginVerificationFragmentDirections.actionLoginVerificationFragmentToSetPinFragment(args.mobilenb.orEmpty())
                )
            }
            is UserState.Registered -> {
                findNavController().navigate(
                 LoginVerificationFragmentDirections.actionLoginVerificationFragmentToSetPinFragment(
                        args.mobilenb.orEmpty()
                    )
                    // LoginVerificationFragmentDirections.actionLoginVerificationFragmentToChangePinRegisterFragment2()

                  /*   LoginVerificationFragmentDirections.actionLoginVerificationFragmentToNavRegister(
                     args.mobilenb.orEmpty()
                     )*/
                )


            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ct.cancel()
        counter = 10
    }

}