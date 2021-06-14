package com.tedmob.africell.features.addNewAccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.benitobertoli.liv.Liv
import com.benitobertoli.liv.rule.NotEmptyRule
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.app.debugOnly
import com.tedmob.africell.data.api.ApiContract
import com.tedmob.africell.data.api.ApiContract.Params.SUB_ACCOUNT_TYPE
import com.tedmob.africell.data.entity.Country
import com.tedmob.africell.features.authentication.CountriesAdapter
import com.tedmob.africell.ui.viewmodel.observeResource
import com.tedmob.africell.ui.viewmodel.observeResourceInline
import com.tedmob.africell.ui.viewmodel.provideViewModel
import com.tedmob.africell.util.getText
import com.tedmob.africell.util.setText
import com.tedmob.africell.util.validation.PhoneNumberHelper
import kotlinx.android.synthetic.main.fragment_mobile_number.*
import kotlinx.android.synthetic.main.fragment_mobile_number.countrySpinner
import kotlinx.android.synthetic.main.fragment_mobile_number.mobileNumberLayout

class AddAccountFragment : BaseFragment(), Liv.Action {

    private var liv: Liv? = null


    private val viewModel by provideViewModel<AddAccountViewModel> { viewModelFactory }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_add_mobile_number, R.layout.toolbar_default, false)
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
            mobileNumberLayout.setText("791122290")
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
            it.indexOfFirst { it.phonecode == ApiContract.Params.SL_PHONE_NUMBER }?.takeIf { it != -1 }?.let {
                countrySpinner.selection = it
            }
            countrySpinner.isEnabled=false
        })
    }

    private fun bindUser() {
        observeResource(viewModel.generateOTPData) {
            findNavController().navigate(R.id.action_addAccountFragment_to_verifyAccountFragment)
        }
    }


    override fun performAction() {
        val phoneCode = (countrySpinner.selectedItem as? Country)?.phonecode
        val formatted =
            PhoneNumberHelper.getFormattedIfValid("", phoneCode + mobileNumberLayout.getText())?.replace("+", "")
        formatted?.let {
            viewModel.generateOTP(formatted,SUB_ACCOUNT_TYPE)
        } ?: showMessage(getString(R.string.phone_number_not_valid))

    }

    override fun onDestroyView() {
        super.onDestroyView()
        liv?.dispose()
    }
}
