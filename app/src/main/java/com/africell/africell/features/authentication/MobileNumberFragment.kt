package com.africell.africell.features.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.africell.africell.Constant.STATIC_PHONE_NUMBER
import com.africell.africell.R
import com.africell.africell.app.debugOnly
import com.africell.africell.app.viewbinding.BaseVBFragment
import com.africell.africell.app.viewbinding.withVBAvailable
import com.africell.africell.data.api.ApiContract.Params.FORGOT_PASSWORD_TYPE
import com.africell.africell.data.api.ApiContract.Params.NEW_USER_TYPE
import com.africell.africell.data.entity.Country
import com.africell.africell.databinding.FragmentMobileNumberBinding
import com.africell.africell.databinding.ToolbarDefaultBinding
import com.africell.africell.ui.viewmodel.observeResource
import com.africell.africell.ui.viewmodel.observeResourceInline
import com.africell.africell.ui.viewmodel.provideViewModel
import com.africell.africell.util.getText
import com.africell.africell.util.navigation.runIfFrom
import com.africell.africell.util.setText
import com.africell.africell.util.validation.PhoneNumberHelper
import com.benitobertoli.liv.Liv
import com.benitobertoli.liv.rule.NotEmptyRule
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MobileNumberFragment : BaseVBFragment<FragmentMobileNumberBinding>(), Liv.Action {

    private var liv: Liv? = null


    private val viewModel by provideViewModel<LoginViewModel>()

    val isReset by lazy {
        arguments?.getBoolean(IS_RESET)
            ?: throw IllegalArgumentException("required Type arguments")
    }

    companion object {
        const val IS_RESET = "is_reset"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(container, FragmentMobileNumberBinding::inflate, false, ToolbarDefaultBinding::inflate)
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

        withVBAvailable {
            sendButton.setOnClickListener { liv?.submitWhenValid() }
        }

        bindUser()
        bindCountries()
        debugOnly {
            withVBAvailable {
                mobileNumberLayout.setText("790790077")
            }
        }
    }

    private fun initLiv(): Liv {
        val notEmptyRule = NotEmptyRule()

        return Liv.Builder()
            .add(requireBinding().mobileNumberLayout, notEmptyRule)
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
        observeResource(viewModel.generateOTPData) {
            val bundle = bundleOf(Pair(IS_RESET, isReset))
            findNavController().runIfFrom(R.id.mobileNumberFragment) {
                navigate(R.id.action_mobileNumberFragment_to_verifyPinFragment, bundle)
            }
        }
    }


    override fun performAction() {
        withVBAvailable {
            val phoneCode = (countrySpinner.selectedItem as? Country)?.phonecode
            val formatted =
                PhoneNumberHelper.getFormattedIfValid("", phoneCode + mobileNumberLayout.getText())?.replace("+", "")
            val otpType = if (isReset == true) FORGOT_PASSWORD_TYPE else NEW_USER_TYPE
            formatted?.let {
                viewModel.generateOTP(formatted, otpType)
            } ?: showMessage(getString(R.string.phone_number_not_valid))
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        liv?.dispose()
    }
}
