package com.tedmob.afrimoney.features.africellservices.databundle


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
import com.tedmob.afrimoney.data.entity.Country
import com.tedmob.afrimoney.databinding.FragmentDataBundleBinding
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.spinner.MaterialSpinner
import com.tedmob.afrimoney.ui.spinner.OnItemSelectedListener
import com.tedmob.afrimoney.ui.viewmodel.observeResource
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
class DataBundleFragment : BaseVBFragmentWithImportContact<FragmentDataBundleBinding>() {

    private val viewModel by provideNavGraphViewModel<DataBundleViewModel>(R.id.nav_databundle)

    private val args by navArgs<DataBundleFragmentArgs>()

    @Inject
    internal lateinit var phoneUtil: PhoneNumberUtil

    var index: Int = -1

    private var validator: FormValidator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentDataBundleBinding::inflate)
    }

    override fun configureToolbar() {
        actionbar?.show()
        actionbar?.title = ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeResource(viewModel.proceedToConfirm) {
            findNavController().navigate(R.id.bundleConfirmationFragment)
        }

        debugOnly {
            binding?.mobileNumberInput?.setText(R.string.debug_number)
        }

        withVBAvailable {
            title.text = args.data.displayName


            wallet.isVisible = false
            mobileNumberInput.isVisible = false
            countryCode.isVisible = false
            radioGroup.isVisible = false
            mobileNumberInput.setEndIconOnClickListener {
                runImportContactFlow()
            }

            validator = setupValidation()
            proceedButton.setDebouncedOnClickListener {
                //  proceed()
                validator?.submit(viewLifecycleOwner.lifecycleScope)
            }
        }

        withVBAvailable {


            bundle.adapter = ArrayAdapter(
                requireContext(),
                R.layout.support_simple_spinner_dropdown_item,
                args.data.bundlelist
            )


            bundle.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: MaterialSpinner,
                    view: View?,
                    position: Int,
                    id: Long
                ) {


                    radioGroup.setOnCheckedChangeListener { group, checkedId ->
                        when (checkedId) {
                            R.id.others -> {
                                mobileNumberInput.isVisible = true
                                countryCode.isVisible = true
                                wallet.selection = -1
                                setWallet(args.data.bundlelist[index].allowedWallets.filter {
                                    it.id != "17"
                                })
                                validator?.stop()
                                validator = setupValidation()
                            }

                            R.id.self -> {
                                mobileNumberInput.isVisible = false
                                countryCode.isVisible = false
                                wallet.selection = -1
                                setWallet(args.data.bundlelist[index].allowedWallets)
                                validator?.stop()
                                validator = setupValidation()
                            }
                        }

                    }


                    index = position
                    radioGroup.isVisible = true
                    if (args.data.bundlelist.get(position).allowedReceiver.size > 1) {
                        self.isChecked = true
                    } else {
                        if (args.data.bundlelist.get(position).allowedReceiver.get(0)
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

                override fun onNothingSelected(parent: MaterialSpinner) {
                }
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


    private fun FragmentDataBundleBinding.setupValidation(): FormValidator = formValidator {
        val notEmptyRule = NotEmptyRule(getString(R.string.mandatory_field))

        if (self.isChecked) {
            bundle.validate(
                notEmptyRule,
            )
            wallet.validate(notEmptyRule)
            onValid = {
                val data = args.data.bundlelist.get(index)
                viewModel.proceed(
                    0,
                    session.msisdn,
                    data.description,
                    data.remark,
                    data.transactionAmount,
                    args.data.receiver_idValue,
                    args.data.receiver_idType,
                    data.BundleId,
                    data.Validity
                )

            }
        } else if (others.isChecked) {
            /*  validatePhoneFields(
                  countryCode.getValidationField(notEmptyRule),
                  mobileNumberInput.getValidationField(notEmptyRule),
                  PhoneRule(
                      getString(R.string.invalid_mobile_number),
                      phoneUtil,
                      type = PhoneRule.Type.MOBILE_OR_UNKNOWN
                  )
              )*/
            bundle.validate(
                notEmptyRule,
            )
            wallet.validate(notEmptyRule)
            mobileNumberInput.validate(notEmptyRule)

            onValid = {
                val data = args.data.bundlelist.get(index)
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
                                data.description,
                                data.remark,
                                data.transactionAmount,
                                args.data.receiver_idValue,
                                args.data.receiver_idType,
                                data.BundleId,
                                data.Validity
                            )
                        }
                    } ?: showMessage(getString(R.string.invalid_mobile_number))
                }

            }
        } else {
            bundle.validate(
                notEmptyRule,
            )
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