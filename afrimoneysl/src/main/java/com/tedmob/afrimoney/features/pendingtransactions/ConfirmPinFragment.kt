package com.tedmob.afrimoney.features.pendingtransactions

import android.os.Bundle
import android.view.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.debugOnly
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.databinding.FragmentConfirmPinBinding

import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.viewmodel.observeResourceFromButton

import com.tedmob.afrimoney.ui.viewmodel.provideViewModel
import com.tedmob.afrimoney.util.getText
import com.tedmob.afrimoney.util.setText

import com.tedmob.libraries.validators.formValidator
import com.tedmob.libraries.validators.rules.NotEmptyRule

import dagger.hilt.android.AndroidEntryPoint

import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class ConfirmPinFragment : BaseVBFragment<FragmentConfirmPinBinding>() {

    @Inject
    lateinit var appSessionNavigator: AppSessionNavigator

    

    private val args by navArgs<ConfirmPinFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentConfirmPinBinding::inflate)
    }


        override fun configureToolbar() {
            actionbar?.show()
            actionbar?.title = ""
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewLifecycleOwner.lifecycleScope.launch {

            debugOnly {
                binding?.pinInputLayout?.setText("4827")
            }
        }


        withVBAvailable {
            val validator = setupValidation()
            proceedButton.setDebouncedOnClickListener { validator.submit(viewLifecycleOwner.lifecycleScope)
            proceedButton.showProgress()}

        }



    }
    private fun FragmentConfirmPinBinding.setupValidation() = formValidator {
        pinInputLayout.validate(NotEmptyRule(getString(R.string.mandatory_field)))

        onValid = {
            proceedWith(pinInputLayout.getText())

        }
    }

    private inline fun proceedWith(pin:String) {
        findNavController().navigate(ConfirmPinFragmentDirections.actionEnterPinFragmentToPendingtransactionsFragment(args.service,pin))


    }

}