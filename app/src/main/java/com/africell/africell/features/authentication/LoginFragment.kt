package com.africell.africell.features.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.africell.africell.BuildConfig.FLAVOR
import com.africell.africell.Constant.STATIC_PHONE_NUMBER
import com.africell.africell.R
import com.africell.africell.app.debugOnly
import com.africell.africell.app.viewbinding.BaseVBFragment
import com.africell.africell.app.viewbinding.withVBAvailable
import com.africell.africell.data.entity.Country
import com.africell.africell.databinding.FragmentLoginBinding
import com.africell.africell.features.authentication.MobileNumberFragment.Companion.IS_RESET
import com.africell.africell.ui.button.observeInView
import com.africell.africell.ui.viewmodel.observeResourceInline
import com.africell.africell.ui.viewmodel.provideViewModel
import com.africell.africell.util.getText
import com.africell.africell.util.setText
import com.africell.africell.util.validation.PhoneNumberHelper
import com.benitobertoli.liv.Liv
import com.benitobertoli.liv.rule.NotEmptyRule
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseVBFragment<FragmentLoginBinding>(), Liv.Action {

    private var liv: Liv? = null


    private val viewModel by provideViewModel<LoginViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentLoginBinding::inflate, false)
    }

    override fun configureToolbar() {
        actionbar?.hide()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        liv = initLiv()
        liv?.start()
        viewModel.getCountries()

        withVBAvailable {
            loginButton.setOnClickListener { liv?.submitWhenValid() }
            signInButton.setOnClickListener {
                val bundle = bundleOf(Pair(IS_RESET, false))
                findNavController().navigate(R.id.action_loginFragment_to_mobileNumberFragment, bundle)
            }
            forgotPassword.setOnClickListener {
                val bundle = bundleOf(Pair(IS_RESET, true))
                findNavController().navigate(R.id.action_loginFragment_to_mobileNumberFragment, bundle)
            }
            skip.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_mainActivity)
                activity?.finish()
            }
        }
        bindUser()
        bindCountries()
        debugOnly {
            withVBAvailable {
                when (FLAVOR) {
                    "sl" -> {
                        mobileNumberLayout.setText("80022041")
                        password.setText("testuser")
                    }
                    "drc" -> {
                        mobileNumberLayout.setText("900099909")
                        password.setText("1234")
                    }
                    "gambia" -> {
                        mobileNumberLayout.setText("2704114")
                        password.setText("notpassword")
                    }
                }


            }
        }
    }

    private fun initLiv(): Liv {
        val notEmptyRule = NotEmptyRule()

        return Liv.Builder()
            .add(requireBinding().mobileNumberLayout, notEmptyRule)
            .add(requireBinding().password, notEmptyRule)
            .submitAction(this)
            .build()
    }

    private fun bindCountries() {
        observeResourceInline(viewModel.countriesData, {
            withVBAvailable {
                countrySpinner.adapter = CountriesAdapter(requireContext(), it)
                it.indexOfFirst { it.phonecode == STATIC_PHONE_NUMBER }?.takeIf { it != -1 }?.let {
                    countrySpinner.selection = it
                }
                countrySpinner.isEnabled = false
            }
        })
    }

    private fun bindUser() {
        viewModel.loginData.observeInView(this, requireBinding().loginButton) {
            onSuccess = {
                button?.isClickable = false
                navigateToMainScreen()

            }
        }
    }

    private fun navigateToMainScreen() {
        findNavController().navigate(R.id.action_loginFragment_to_mainActivity)
        activity?.finish()
    }

    override fun performAction() {
        withVBAvailable {
            val phoneCode = (countrySpinner.selectedItem as? Country)?.phonecode?.replace("+", "")
            val formatted = PhoneNumberHelper.getFormattedIfValid("", phoneCode + mobileNumberLayout.getText())
                ?.replace("+", "")

            formatted?.let {
                viewModel.login(it, password.getText())
            } ?: showMessage(getString(R.string.phone_number_not_valid))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        liv?.dispose()
    }
}
