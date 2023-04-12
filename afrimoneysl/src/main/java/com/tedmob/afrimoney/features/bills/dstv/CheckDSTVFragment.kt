package com.tedmob.afrimoney.features.bills.dstv

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
import com.tedmob.afrimoney.app.debugOnly
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.data.entity.IdTypeItem
import com.tedmob.afrimoney.databinding.FragmentCheckDstvBinding
import com.tedmob.afrimoney.databinding.FragmentRenewDstvBinding
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.spinner.MaterialSpinner
import com.tedmob.afrimoney.ui.spinner.OnItemSelectedListener
import com.tedmob.afrimoney.ui.viewmodel.observeResource
import com.tedmob.afrimoney.ui.viewmodel.observeResourceFromButton
import com.tedmob.afrimoney.ui.viewmodel.provideNavGraphViewModel
import com.tedmob.afrimoney.util.getText
import com.tedmob.afrimoney.util.phone.BaseVBFragmentWithImportContact
import com.tedmob.afrimoney.util.setText
import com.tedmob.libraries.validators.FormValidator
import com.tedmob.libraries.validators.formValidator
import com.tedmob.libraries.validators.rules.NotEmptyRule
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class CheckDSTVFragment : BaseVBFragmentWithImportContact<FragmentCheckDstvBinding>() {

    private val viewModel by provideNavGraphViewModel<CheckDSTVViewModel>(R.id.nav_dstv)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentCheckDstvBinding::inflate)
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
        debugOnly {
            withVBAvailable {
                smartCard.setText("7029112126")
            }
        }


        withVBAvailable {

            image.load(R.drawable.paymybillsdstv) {
                transformations(CircleCropTransformation())
            }

            val validator = setupValidation()

            proceedButton.setDebouncedOnClickListener { validator.submit(viewLifecycleOwner.lifecycleScope) }
        }


    }

    private fun FragmentCheckDstvBinding.setupValidation(): FormValidator = formValidator {
        val notEmptyRule = NotEmptyRule(getString(R.string.mandatory_field))

        smartCard.validate(
            notEmptyRule,
        )


        onValid = {
            viewModel.proceed(smartCard.getText())

        }
    }


    private fun proceed() {

        findNavController().navigate(
            CheckDSTVFragmentDirections.actionCheckDSTVFragmentToCheckDSTVConfirmationFragment()
        )
    }


}