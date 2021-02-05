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
import com.tedmob.africell.data.api.ApiContract.Params.FORGOT_PASSWORD_TYPE
import com.tedmob.africell.data.api.ApiContract.Params.NEW_USER_TYPE
import com.tedmob.africell.data.entity.Country
import com.tedmob.africell.ui.viewmodel.ViewModelFactory
import com.tedmob.africell.ui.viewmodel.observeResource
import com.tedmob.africell.ui.viewmodel.observeResourceInline
import com.tedmob.africell.ui.viewmodel.provideViewModel
import com.tedmob.africell.util.getText
import com.tedmob.africell.util.setText
import com.tedmob.africell.util.validation.PhoneNumberHelper
import kotlinx.android.synthetic.main.fragment_mobile_number.*
import javax.inject.Inject

class MobileNumberFragment : BaseFragment(), Liv.Action {

    private var liv: Liv? = null


    private val viewModel by provideViewModel<LoginViewModel> { viewModelFactory }

    val isReset by lazy {
        arguments?.getBoolean(IS_RESET)
            ?: throw IllegalArgumentException("required Type arguments")
    }

    companion object {
        const val IS_RESET = "is_reset"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_mobile_number, R.layout.toolbar_default, false)
    }

    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.setDisplayHomeAsUpEnabled(false)
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_back)
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.title = ""
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        liv = initLiv()
        liv?.start()
        viewModel.getCountries()

        sendButton.setOnClickListener { liv?.submitWhenValid() }

        bindUser()
        bindCountries()
        debugOnly {
            mobileNumberLayout.setText("790790077")
        }
    }

    private fun initLiv(): Liv {
        val notEmptyRule = NotEmptyRule()

        return Liv.Builder()
            .add(mobileNumberLayout, notEmptyRule)
            .submitAction(this)
            .build()
    }

    private fun bindCountries() {
        observeResourceInline(viewModel.countriesData, {
            countrySpinner.adapter = CountriesAdapter(requireContext(), it)
            it.indexOfFirst { it.phonecode == "+256" }?.takeIf { it != -1 }?.let {
                countrySpinner.selection = it
            }
        })
    }

    private fun bindUser() {
        observeResource(viewModel.generateOTPData) {
            val bundle = bundleOf(Pair(IS_RESET, isReset))
            findNavController().navigate(R.id.action_mobileNumberFragment_to_verifyPinFragment, bundle)
        }
    }


    override fun performAction() {
        val phoneCode = (countrySpinner.selectedItem as? Country)?.phonecode
        val formatted =
            PhoneNumberHelper.getFormattedIfValid("", phoneCode + mobileNumberLayout.getText())?.replace("+", "")
        val otpType= if(isReset==true) FORGOT_PASSWORD_TYPE else NEW_USER_TYPE
        formatted?.let {
            viewModel.generateOTP(formatted,otpType)
        } ?: showMessage(getString(R.string.phone_number_not_valid))

    }

    override fun onDestroyView() {
        super.onDestroyView()
        liv?.dispose()
    }
}
