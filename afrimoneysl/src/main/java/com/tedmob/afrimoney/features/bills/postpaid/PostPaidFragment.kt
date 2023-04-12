package com.tedmob.afrimoney.features.bills.postpaid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.data.entity.IdTypeItem
import com.tedmob.afrimoney.databinding.FragmentCheckDstvBinding
import com.tedmob.afrimoney.databinding.FragmentPostPaidBinding
import com.tedmob.afrimoney.databinding.FragmentPowergenBinding
import com.tedmob.afrimoney.databinding.FragmentRenewDstvBinding
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.spinner.MaterialSpinner
import com.tedmob.afrimoney.ui.spinner.OnItemSelectedListener
import com.tedmob.afrimoney.ui.viewmodel.observeResource
import com.tedmob.afrimoney.ui.viewmodel.observeResourceFromButton
import com.tedmob.afrimoney.ui.viewmodel.provideNavGraphViewModel
import com.tedmob.afrimoney.util.getText
import com.tedmob.afrimoney.util.phone.BaseVBFragmentWithImportContact
import com.tedmob.libraries.validators.FormValidator
import com.tedmob.libraries.validators.formValidator
import com.tedmob.libraries.validators.rules.NotEmptyRule
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class PostPaidFragment : BaseVBFragmentWithImportContact<FragmentPostPaidBinding>() {

    private val viewModel by provideNavGraphViewModel<PostPaidViewModel>(R.id.nav_postpaid)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentPostPaidBinding::inflate)
    }

    override fun configureToolbar() {
        actionbar?.show()
        actionbar?.title = ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





        observeResourceFromButton(viewModel.proceedToConfirm, R.id.proceedButton) {
            proceed()
        }



        withVBAvailable {

            image.load(R.drawable.ic_pay_my_bills_postpaid) {
                transformations(CircleCropTransformation())
            }


            val validator = setupValidation()

            proceedButton.setDebouncedOnClickListener { validator.submit(viewLifecycleOwner.lifecycleScope) }
        }


    }

    private fun FragmentPostPaidBinding.setupValidation(): FormValidator = formValidator {
        val notEmptyRule = NotEmptyRule(getString(R.string.mandatory_field))

        mobileNumber.validate(notEmptyRule)

        amount.validate(notEmptyRule)


        onValid = {
            viewModel.proceed(mobileNumber.getText(), amount.getText())

        }
    }


    private fun proceed() {


        findNavController().navigate(PostPaidFragmentDirections.actionPostPaidFragmentToPostPaidConfirmationFragment())
    }


}