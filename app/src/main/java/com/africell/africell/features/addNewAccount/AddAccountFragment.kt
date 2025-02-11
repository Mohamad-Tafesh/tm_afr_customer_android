package com.africell.africell.features.addNewAccount

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
import com.africell.africell.data.api.ApiContract.Params.SUB_ACCOUNT_TYPE
import com.africell.africell.data.entity.Country
import com.africell.africell.databinding.FragmentAddMobileNumberBinding
import com.africell.africell.databinding.ToolbarDefaultBinding
import com.africell.africell.features.addNewAccount.VerifyAccountFragment.Companion.MSISDN_KEY
import com.africell.africell.features.authentication.CountriesAdapter
import com.africell.africell.ui.viewmodel.observeResource
import com.africell.africell.ui.viewmodel.observeResourceInline
import com.africell.africell.ui.viewmodel.provideViewModel
import com.africell.africell.util.getText
import com.africell.africell.util.setText
import com.africell.africell.util.validation.PhoneNumberHelper
import com.benitobertoli.liv.Liv
import com.benitobertoli.liv.rule.NotEmptyRule
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddAccountFragment : BaseVBFragment<FragmentAddMobileNumberBinding>(), Liv.Action {

    private var liv: Liv? = null


    private val viewModel by provideViewModel<AddAccountViewModel>()
    var formatted: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(
            container,
            FragmentAddMobileNumberBinding::inflate,
            false,
            ToolbarDefaultBinding::inflate
        )
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
            sendButton.setOnClickListener {
                liv?.submitWhenValid()
            }
        }

        bindUser()
        bindCountries()
        debugOnly {
            withVBAvailable {
                mobileNumberLayout.setText("791122290")
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
            val bundle = bundleOf(Pair(MSISDN_KEY, formatted))
            findNavController().navigate(R.id.action_addAccountFragment_to_verifyAccountFragment, bundle)
        }
    }


    override fun performAction() {
        withVBAvailable {
            val phoneCode = (countrySpinner.selectedItem as? Country)?.phonecode
            formatted =
                PhoneNumberHelper.getFormattedIfValid("", phoneCode + mobileNumberLayout.getText())?.replace("+", "")
            formatted?.let {
                viewModel.generateOTP(it, SUB_ACCOUNT_TYPE)
            } ?: showMessage(getString(R.string.phone_number_not_valid))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        liv?.dispose()
    }
}
