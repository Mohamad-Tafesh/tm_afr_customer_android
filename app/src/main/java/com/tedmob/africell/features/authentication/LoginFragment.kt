package com.tedmob.africell.features.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.benitobertoli.liv.Liv
import com.benitobertoli.liv.rule.NotEmptyRule
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.app.debugOnly
import com.tedmob.africell.data.api.ApiContract
import com.tedmob.africell.data.entity.Country
import com.tedmob.africell.features.authentication.MobileNumberFragment.Companion.IS_RESET
import com.tedmob.africell.ui.button.observeInView
import com.tedmob.africell.ui.viewmodel.observeResourceInline
import com.tedmob.africell.ui.viewmodel.provideViewModel
import com.tedmob.africell.util.getText
import com.tedmob.africell.util.setText
import com.tedmob.africell.util.validation.PhoneNumberHelper
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment(), Liv.Action {

    private var liv: Liv? = null


    private val viewModel by provideViewModel<LoginViewModel> { viewModelFactory }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return wrap(inflater.context, R.layout.fragment_login, 0, false)
    }

    override fun configureToolbar() {
        actionbar?.hide()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        liv = initLiv()
        liv?.start()
        viewModel.getCountries()

        loginButton.setOnClickListener { liv?.submitWhenValid() }
        signInButton.setOnClickListener {
            val bundle= bundleOf(Pair(IS_RESET,false))
            findNavController().navigate(R.id.action_loginFragment_to_mobileNumberFragment,bundle)
        }
        forgotPassword.setOnClickListener {
            val bundle= bundleOf(Pair(IS_RESET,true))
            findNavController().navigate(R.id.action_loginFragment_to_mobileNumberFragment,bundle)
        }
        skip.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_mainActivity)
            activity?.finish()
        }
        bindUser()
        bindCountries()
        debugOnly {
            mobileNumberLayout.setText("2704114")
            password.setText("testpass")

        }
    }

    private fun initLiv(): Liv {
        val notEmptyRule = NotEmptyRule()

        return Liv.Builder()
            .add(mobileNumberLayout, notEmptyRule)
            .add(password,notEmptyRule)
            .submitAction(this)
            .build()
    }

    private fun bindCountries() {
        observeResourceInline(viewModel.countriesData, {
            countrySpinner.adapter = CountriesAdapter(requireContext(), it)
            it.indexOfFirst { it.phonecode == ApiContract.Params.SL_PHONE_NUMBER }?.takeIf { it != -1 }?.let {
                countrySpinner.selection = it
            }
            countrySpinner.isEnabled=false
        })
    }

    private fun bindUser() {
        viewModel.loginData.observeInView(this, loginButton) {
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
        val phoneCode = (countrySpinner.selectedItem as? Country)?.phonecode?.replace("+","")
        val formatted = PhoneNumberHelper.getFormattedIfValid("", phoneCode + mobileNumberLayout.getText())?.replace("+","")
        formatted?.let {
            viewModel.login(it,password.getText())
        } ?: showMessage(getString(R.string.phone_number_not_valid))

    }

    override fun onDestroyView() {
        super.onDestroyView()
        liv?.dispose()
    }
}
