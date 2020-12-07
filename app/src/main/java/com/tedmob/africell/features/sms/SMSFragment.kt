package com.tedmob.africell.features.sms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.benitobertoli.liv.Liv
import com.benitobertoli.liv.rule.NotEmptyRule
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.data.entity.Country
import com.tedmob.africell.features.authentication.CountriesAdapter
import com.tedmob.africell.ui.viewmodel.ViewModelFactory
import com.tedmob.africell.ui.viewmodel.observeResource
import com.tedmob.africell.ui.viewmodel.observeResourceInline
import com.tedmob.africell.ui.viewmodel.provideViewModel
import com.tedmob.africell.util.getText
import com.tedmob.africell.util.setText
import com.tedmob.africell.util.validation.PhoneNumberHelper
import kotlinx.android.synthetic.main.fragment_sms.*
import javax.inject.Inject


class SMSFragment : BaseFragment(), Liv.Action {
    private var liv: Liv? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by provideViewModel<SMSViewModel> { viewModelFactory }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_sms, R.layout.toolbar_default, true)
    }

    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.title = getString(R.string.send_free_sms)
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_side_menu)
        actionbar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        liv = initLiv()
        liv?.start()
        viewModel.getSmsCount()
        sendBtn.setOnClickListener { liv?.submitWhenValid() }

        bindData()

    }

    private fun initLiv(): Liv {
        val notEmptyRule = NotEmptyRule()

        return Liv.Builder()
            .add(mobileNumberLayout, notEmptyRule)
            .add(messageLayout, notEmptyRule)
            .submitAction(this)
            .build()

    }

    private fun bindData() {
        observeResourceInline(viewModel.smsData) {

            recyclerView.adapter = SMSAdapter(it.smsCount.smsCount ?: 0)
            val countries = it.countries
            countrySpinner.adapter = CountriesAdapter(requireContext(), countries)
            countries.indexOfFirst { it.phonecode == "+961" }?.takeIf { it != -1 }?.let {
                countrySpinner.selection = it
            }
        }

        observeResource(viewModel.smsSentData) {
            showMessageDialog(it.resultText.orEmpty(), getString(R.string.close)) {
                recyclerView.adapter = SMSAdapter(it.smsCount ?: 0)
                liv?.dispose()
                messageLayout.setText("")
                mobileNumberLayout.setText("")
                liv?.start()
            }
        }
    }


    override fun performAction() {
        val phoneCode = (countrySpinner.selectedItem as? Country)?.phonecode?.replace("+", "")
        val formatted =
            PhoneNumberHelper.getFormattedIfValid("", phoneCode + mobileNumberLayout.getText())?.replace("+", "")
        formatted?.let {
            viewModel.sendSMS(it, messageLayout.getText())
        } ?: showMessage(getString(R.string.phone_number_not_valid))

    }

    override fun onDestroyView() {
        super.onDestroyView()
        liv?.dispose()
    }
}

