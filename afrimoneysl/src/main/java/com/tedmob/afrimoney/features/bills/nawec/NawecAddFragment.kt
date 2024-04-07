package com.tedmob.afrimoney.features.bills.nawec

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.debugOnly
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.databinding.FragmentNawecAddBinding
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.viewmodel.observeResourceFromButton
import com.tedmob.afrimoney.ui.viewmodel.provideNavGraphViewModel
import com.tedmob.afrimoney.util.getText
import com.tedmob.afrimoney.util.phone.BaseVBFragmentWithImportContact
import com.tedmob.afrimoney.util.setText
import com.tedmob.libraries.validators.FormValidator
import com.tedmob.libraries.validators.formValidator
import com.tedmob.libraries.validators.rules.NotEmptyRule
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NawecAddFragment : BaseVBFragmentWithImportContact<FragmentNawecAddBinding>() {

    private val viewModel by provideNavGraphViewModel<NawecViewModel>(R.id.nav_nawec)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentNawecAddBinding::inflate)
    }

    override fun configureToolbar() {
        actionbar?.show()
        actionbar?.title = ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        debugOnly {
            withVBAvailable {
                meterNumber.setText("04040404107")
            }
        }



        observeResourceFromButton(viewModel.data, R.id.proceedButton) {
            proceed()
        }



        withVBAvailable {


            val validator = setupValidation()

            proceedButton.setDebouncedOnClickListener { validator.submit(viewLifecycleOwner.lifecycleScope) }
        }


    }

    private fun FragmentNawecAddBinding.setupValidation(): FormValidator = formValidator {
        val notEmptyRule = NotEmptyRule(getString(R.string.mandatory_field))

        meterNumber.validate(notEmptyRule)



        onValid = {
            viewModel.proceed(meterNumber.getText())

        }
    }


    private fun proceed() {
        findNavController().navigate(NawecAddFragmentDirections.actionNawecAddFragmentToNawecAddConfirmationFragment())
    }


}