package com.tedmob.afrimoney.features.bills.fcc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.databinding.FragmentFccBinding
import com.tedmob.afrimoney.databinding.FragmentMercuryBinding
import com.tedmob.afrimoney.features.bills.mercury.MercuryFragmentDirections
import com.tedmob.afrimoney.features.bills.mercury.MercuryViewModel
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.viewmodel.observeResource
import com.tedmob.afrimoney.ui.viewmodel.observeResourceFromButton
import com.tedmob.afrimoney.ui.viewmodel.provideNavGraphViewModel
import com.tedmob.afrimoney.util.getText
import com.tedmob.afrimoney.util.phone.BaseVBFragmentWithImportContact
import com.tedmob.libraries.validators.FormValidator
import com.tedmob.libraries.validators.formValidator
import com.tedmob.libraries.validators.rules.NotEmptyRule
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FccFragment : BaseVBFragmentWithImportContact<FragmentFccBinding>() {

    private val viewModel by provideNavGraphViewModel<FccViewModel>(R.id.nav_fcc)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentFccBinding::inflate)
    }

    override fun configureToolbar() {
        actionbar?.show()
        actionbar?.title = ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        observeResourceFromButton(viewModel.proceedToConfirm,R.id.proceedButton) {
            proceed()
        }



        withVBAvailable {

            image.load(R.drawable.paymybillsmercury){
                transformations(CircleCropTransformation())
            }


            val validator = setupValidation()

            proceedButton.setDebouncedOnClickListener { validator.submit(viewLifecycleOwner.lifecycleScope) }
        }


    }

    private fun FragmentFccBinding.setupValidation(): FormValidator = formValidator {
        val notEmptyRule = NotEmptyRule(getString(R.string.mandatory_field))

        firstName.validate(notEmptyRule)

        lastName.validate(notEmptyRule)

        address.validate(notEmptyRule)


        onValid = {
            viewModel.proceed(firstName.getText(),lastName.getText(),address.getText())

        }
    }



    private fun proceed() {

        viewModel.getAmount()
        observeResource(viewModel.data){
            findNavController().navigate(FccFragmentDirections.actionFccFragmentToFccConfirmationFragment())

        }
        }


}