package com.africell.africell.features.profile

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.africell.africell.R
import com.africell.africell.app.viewbinding.BaseVBFragment
import com.africell.africell.app.viewbinding.withVBAvailable
import com.africell.africell.data.api.ApiContract
import com.africell.africell.databinding.FragmentEditProfileBinding
import com.africell.africell.databinding.ToolbarImageBinding
import com.africell.africell.ui.hideKeyboard
import com.africell.africell.ui.viewmodel.observe
import com.africell.africell.ui.viewmodel.observeResource
import com.africell.africell.ui.viewmodel.observeResourceInline
import com.africell.africell.ui.viewmodel.provideActivityViewModel
import com.africell.africell.util.*
import com.benitobertoli.liv.Liv
import com.benitobertoli.liv.rule.EmailRule
import com.benitobertoli.liv.rule.NotEmptyRule
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class EditProfileFragment : BaseVBFragment<FragmentEditProfileBinding>(), Liv.Action {

    private val viewModel by provideActivityViewModel<EditProfileViewModel>()
    private var liv: Liv? = null

    private val onDestinationChangedListener: NavController.OnDestinationChangedListener =
        NavController.OnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == R.id.loginFragment) {
                activity?.hideKeyboard()
            }
        }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(container, FragmentEditProfileBinding::inflate, true, ToolbarImageBinding::inflate)
    }

    override fun configureToolbar() {
        getToolbarBindingAs<ToolbarImageBinding>()?.run {
            actionbar?.setHomeAsUpIndicator(R.mipmap.nav_back)
            toolbarImage.setActualImageResource(R.mipmap.main_top3)
            actionbar?.setDisplayHomeAsUpEnabled(true)
            actionbar?.title = ""
            toolbarTitle.text = getString(R.string.my_profile)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getToolbarBindingAs<ToolbarImageBinding>()?.run {
            setupImageBanner(toolbarImage, ApiContract.Params.BANNERS, ApiContract.ImagePageName.PROFILE)
        }
        withVBAvailable {
            liv = initLiv()
            liv?.start()
            setupDOB()
            bindData()
            findNavController().addOnDestinationChangedListener(onDestinationChangedListener)
            changePasswordButton.setOnClickListener {
                findNavController().navigate(R.id.action_editProfileFragment_to_changePasswordFragment)
            }
            updateProfileButton.setOnClickListener {
                liv?.submitWhenValid()
            }
        }


    }

    private fun bindData() {
        viewModel.getProfile()
        observeResourceInline(viewModel.profileData) {
            withVBAvailable {
                emailLayout.setTextWhenNotBlank(it.email)
                firstName.setTextWhenNotBlank(it.firstName)
                lastName.setTextWhenNotBlank(it.lastName)
                viewModel.dobData.value = it.dateOfBirth?.toTimeInMillis(DOB_DATE_FORMAT)
            }
        }
        observeResource(viewModel.updatedProfileData) {
            showMaterialMessageDialog(
                getString(R.string.your_profile_has_been_update_successfully),
                getString(R.string.close)
            ) {
                findNavController().popBackStack()
            }
        }
    }


    private fun FragmentEditProfileBinding.setupDOB() {
        observe(viewModel.dobData, { timestamp ->
            withVBAvailable {
                timestamp?.let {
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = timestamp
                    dobLayout.setText(EditProfileViewModel.dateFormat.format(calendar.time))
                }
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


    private fun FragmentEditProfileBinding.initLiv(): Liv {
        val notEmptyRule = NotEmptyRule()
        val emailRule = EmailRule(getString(R.string.invalid_email))
        return Liv.Builder()
            .add(firstName, notEmptyRule)
            .add(lastName, notEmptyRule)
            .submitAction(this@EditProfileFragment)
            .build()
    }


    override fun performAction() {
        withVBAvailable {
            viewModel.setProfile(
                firstName.getText(),
                lastName.getText(),
                emailLayout.getText()
            )
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        findNavController().removeOnDestinationChangedListener(onDestinationChangedListener)
        liv?.dispose()
    }


}
