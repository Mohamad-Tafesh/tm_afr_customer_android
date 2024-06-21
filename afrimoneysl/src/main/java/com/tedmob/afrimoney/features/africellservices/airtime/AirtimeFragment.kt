package com.tedmob.afrimoney.features.africellservices.airtime


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.debugOnly
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.data.entity.AllowedWallets
import com.tedmob.afrimoney.data.entity.Bundlelist
import com.tedmob.afrimoney.data.entity.BundlelistParent
import com.tedmob.afrimoney.data.entity.Country
import com.tedmob.afrimoney.databinding.FragmentAirtimeBinding
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.spinner.MaterialSpinner
import com.tedmob.afrimoney.ui.spinner.OnItemSelectedListener
import com.tedmob.afrimoney.ui.viewmodel.observeResource
import com.tedmob.afrimoney.ui.viewmodel.observeResourceInline
import com.tedmob.afrimoney.ui.viewmodel.provideNavGraphViewModel
import com.tedmob.afrimoney.util.getText
import com.tedmob.afrimoney.util.phone.BaseVBFragmentWithImportContact
import com.tedmob.afrimoney.util.phone.PhoneNumber2Helper
import com.tedmob.afrimoney.util.setText
import com.tedmob.libraries.validators.FormValidator
import com.tedmob.libraries.validators.formValidator
import com.tedmob.libraries.validators.rules.NotEmptyRule
import com.tedmob.libraries.validators.rules.phone.PhoneRule
import dagger.hilt.android.AndroidEntryPoint
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import javax.inject.Inject


@AndroidEntryPoint
class AirtimeFragment : BaseVBFragmentWithImportContact<FragmentAirtimeBinding>() {

    private val viewModel by provideNavGraphViewModel<AirtimeViewModel>(R.id.nav_airtime)

    @Inject
    internal lateinit var phoneUtil: PhoneNumberUtil


    var index: Int = -1


    private var validator: FormValidator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentAirtimeBinding::inflate, true)
    }

    override fun configureToolbar() {
        actionbar?.show()
        actionbar?.title = ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getServices(getString(R.string.currency))



        observeResource(viewModel.proceedToConfirm) {
            findNavController().navigate(R.id.airtimeConfirmationfragment)
        }

        debugOnly {
            binding?.mobileNumberInput?.setText(R.string.debug_number)
        }

        withVBAvailable {

            wallet.isVisible = false
            mobileNumberInput.isVisible = false
            radioGroup.isVisible = false
            mobileNumberInput.setEndIconOnClickListener {
                runImportContactFlow()
            }


            proceedButton.setDebouncedOnClickListener {
                validator?.submit(viewLifecycleOwner.lifecycleScope)
            }

            observeResourceInline(viewModel.data) {
                validator = setupValidation(it, it.bundlelist.get(0))
                setData(it)
            }
        }


    }

    fun setWallet(list: List<AllowedWallets>) {
        binding?.wallet?.adapter = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            list
        )
    }

    fun setData(data: BundlelistParent) {
        withVBAvailable {


            radioGroup.setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.others -> {
                        mobileNumberInput.isVisible = true
                        countryCode.isVisible = true
                        wallet.selection = -1
                        setWallet(data.bundlelist[0].allowedWallets.filter {
                            it.id != "17"
                        })
                        validator?.stop()
                        validator = setupValidation(data, data.bundlelist.get(0))
                    }

                    R.id.self -> {
                        mobileNumberInput.isVisible = false
                        countryCode.isVisible = false
                        wallet.selection = -1
                        setWallet(data.bundlelist[0].allowedWallets)
                        validator?.stop()
                        validator = setupValidation(data, data.bundlelist.get(0))
                    }
                }

            }



            radioGroup.isVisible = true
            if (data.bundlelist.get(0).allowedReceiver.size > 1) {
                self.isChecked = true
            } else {
                if (data.bundlelist.get(0).allowedReceiver.get(0)
                        .equals("self", true)
                ) {
                    self.isChecked = true
                    others.isVisible = false
                } else {
                    others.isChecked = true
                    self.isVisible = false
                }
            }
            wallet.isVisible = true


        }


    }


    private fun FragmentAirtimeBinding.setupValidation(
        data: BundlelistParent,
        list: Bundlelist
    ): FormValidator = formValidator {
        val notEmptyRule = NotEmptyRule(getString(R.string.mandatory_field))

        if (self.isChecked) {
            wallet.validate(notEmptyRule)
            amountInput.validate(notEmptyRule)
            onValid = {

                if ((amountInput.getText().toDoubleOrNull() ?: 0.0) > 0) {

                    viewModel.proceed(
                        0,
                        session.msisdn,
                        list.description,
                        list.remark,
                        amountInput.getText(),
                        data.receiver_idValue,
                        data.receiver_idType,
                        list.BundleId,
                        list.Validity
                    )
                } else {
                    showMessage("Please enter your amount")
                }


            }
        } else if (others.isChecked) {
            /*            validatePhoneFields(
                            countryCode.getValidationField(notEmptyRule),
                            mobileNumberInput.getValidationField(notEmptyRule),
                            PhoneRule(
                                getString(R.string.invalid_mobile_number),
                                phoneUtil,
                                type = PhoneRule.Type.MOBILE_OR_UNKNOWN
                            )
                        )*/
            mobileNumberInput.validate(notEmptyRule)
            wallet.validate(notEmptyRule)
            amountInput.validate(notEmptyRule)
            onValid = {

                if ((amountInput.getText().toDoubleOrNull() ?: 0.0) > 0) {
                    var number = mobileNumberInput.getText()


                    withVBAvailable {
                        val formatted =
                            if (number.length == 7 && ((number.startsWith("2") || number.startsWith("4") || number.startsWith("7")))) {
                                "Done"
                            } else null

                        formatted?.let {
                            number.let {
                                viewModel.proceed(
                                    1,
                                    it,
                                    list.description,
                                    list.remark,
                                    amountInput.getText(),
                                    data.receiver_idValue,
                                    data.receiver_idType,
                                    list.BundleId,
                                    list.Validity
                                )
                            }
                        } ?: showMessage(getString(R.string.invalid_mobile_number))
                    }


                } else {
                    showMessage("Please enter your amount")
                }


            }
        } else {
            onValid = {
            }
        }


    }


    private fun runImportContactFlow() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            importContact(Country.DEFAULT_CODE)?.let { binding?.mobileNumberInput?.setText(it) }
        }
    }


}