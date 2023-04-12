package com.tedmob.afrimoney.features.bills.covid

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
import com.tedmob.afrimoney.databinding.FragmentCovidBinding
import com.tedmob.afrimoney.databinding.FragmentEdsaBinding
import com.tedmob.afrimoney.databinding.FragmentMercuryBinding
import com.tedmob.afrimoney.databinding.FragmentPostPaidBinding
import com.tedmob.afrimoney.features.bills.postpaid.PostPaidFragmentDirections
import com.tedmob.afrimoney.features.bills.postpaid.PostPaidViewModel
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
class CovidFragment : BaseVBFragmentWithImportContact<FragmentCovidBinding>() {

    private val viewModel by provideNavGraphViewModel<CovidViewModel>(R.id.nav_covid)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentCovidBinding::inflate)
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


            val validator = setupValidation()

            proceedButton.setDebouncedOnClickListener { validator.submit(viewLifecycleOwner.lifecycleScope) }
        }


    }

    private fun FragmentCovidBinding.setupValidation(): FormValidator = formValidator {
        val notEmptyRule = NotEmptyRule(getString(R.string.mandatory_field))

        idNumber.validate(notEmptyRule)


        onValid = {
            viewModel.getAmount()
        }
    }


    private fun proceed() {
        observeResource(viewModel.data) {
            findNavController().navigate(CovidFragmentDirections.actionCovidFragmentToCovidConfirmationFragment())

        }
    }


}