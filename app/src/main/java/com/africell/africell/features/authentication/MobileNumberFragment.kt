package com.africell.africell.features.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.benitobertoli.liv.Liv
import com.benitobertoli.liv.rule.NotEmptyRule
import com.africell.africell.Constant.STATIC_PHONE_NUMBER
import com.africell.africell.R
import com.africell.africell.app.BaseFragment
import com.africell.africell.app.debugOnly
import com.africell.africell.data.api.ApiContract.Params.FORGOT_PASSWORD_TYPE
import com.africell.africell.data.api.ApiContract.Params.NEW_USER_TYPE
import com.africell.africell.data.entity.Country
import com.africell.africell.ui.viewmodel.observeResource
import com.africell.africell.ui.viewmodel.observeResourceInline
import com.africell.africell.ui.viewmodel.provideViewModel
import com.africell.africell.util.getText
import com.africell.africell.util.navigation.runIfFrom
import com.africell.africell.util.setText
import com.africell.africell.util.validation.PhoneNumberHelper
import kotlinx.android.synthetic.main.fragment_mobile_number.*
import kotlinx.android.synthetic.main.fragment_mobile_number.countrySpinner
import kotlinx.android.synthetic.main.fragment_mobile_number.mobileNumberLayout

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
            it.indexOfFirst { it.phonecode == STATIC_PHONE_NUMBER  }?.takeIf { it != -1 }?.let {
                countrySpinner.selection = it
            }
            countrySpinner.isEnabled=false
        })
    }

    private fun bindUser() {
        observeResource(viewModel.generateOTPData) {
            val bundle = bundleOf(Pair(IS_RESET, isReset))
            findNavController().runIfFrom(R.id.mobileNumberFragment) {
                navigate(R.id.action_mobileNumberFragment_to_verifyPinFragment, bundle)
            }
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
