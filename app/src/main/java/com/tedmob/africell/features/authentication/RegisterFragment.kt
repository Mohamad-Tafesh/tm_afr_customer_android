package com.tedmob.africell.features.authentication

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.benitobertoli.liv.Liv
import com.benitobertoli.liv.rule.EmailRule
import com.benitobertoli.liv.rule.NotEmptyRule
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.data.api.ApiContract
import com.tedmob.africell.ui.hideKeyboard
import com.tedmob.africell.ui.viewmodel.observe
import com.tedmob.africell.ui.viewmodel.observeResource
import com.tedmob.africell.ui.viewmodel.provideActivityViewModel
import com.tedmob.africell.util.DatePickerFragment
import com.tedmob.africell.util.getText
import com.tedmob.africell.util.setText
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.toolbar_image.*
import java.util.*

class RegisterFragment : BaseFragment(), Liv.Action {

    private val viewModel by provideActivityViewModel<RegisterViewModel> { viewModelFactory }
    private var liv: Liv? = null

    private val onDestinationChangedListener: NavController.OnDestinationChangedListener =
        NavController.OnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == R.id.loginFragment) {
                activity?.hideKeyboard()
            }
        }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_register, R.layout.toolbar_image, false)
    }

    override fun configureToolbar() {
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_back)
        toolbarImage.setActualImageResource(R.mipmap.main_top3)
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.title = ""
        toolbarTitle.text = getString(R.string.signing_up)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupImageBanner(toolbarImage, ApiContract.Params.BANNERS, ApiContract.ImagePageName.SIGN_UP)
        liv = initLiv()
        liv?.start()
        setupDOB()
        bindData()
        findNavController().addOnDestinationChangedListener(onDestinationChangedListener)

        nextButton.setOnClickListener {
            activity?.hideKeyboard()
            liv?.submitWhenValid()
        }


    }

    private fun bindData() {
        observeResource(viewModel.updatedProfileData) {
            findNavController().navigate(R.id.action_registerFragment_to_mainActivity)
            activity?.finish()
        }
    }


    private fun setupDOB() {
        observe(viewModel.dobData, { timestamp ->
            timestamp?.let {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = timestamp
                dobLayout.setText(RegisterViewModel.dateFormat.format(calendar.time))
            }
        })

        dobEditText.setOnClickListener {
            val datePicker = DatePickerFragment.newInstance(viewModel.dobData.value)
            datePicker.setOnDateSetListener(DatePickerDialog.OnDateSetListener { _, year, month, day ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                viewModel.dobData.value = calendar.timeInMillis
            })

            datePicker.setAge18()
            datePicker.show(childFragmentManager, "dob-picker")
        }

    }


    private fun initLiv(): Liv {
        val notEmptyRule = NotEmptyRule()
        val emailRule = EmailRule(getString(R.string.invalid_email))
        return Liv.Builder()
            .add(firstName, notEmptyRule)
            .add(lastName, notEmptyRule)
            .submitAction(this)
            .build()
    }


    override fun performAction() {
        viewModel.setProfile(
            firstName.getText(),
            lastName.getText(),
            emailLayout.getText(),
            passwordLayout.getText(),
            confirmPasswordLayout.getText()
        )
    }


    override fun onDestroy() {
        super.onDestroy()
        findNavController().removeOnDestinationChangedListener(onDestinationChangedListener)
        liv?.dispose()
    }


}
