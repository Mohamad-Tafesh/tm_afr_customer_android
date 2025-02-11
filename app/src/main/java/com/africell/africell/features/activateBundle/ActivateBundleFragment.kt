package com.africell.africell.features.activateBundle

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.africell.africell.Constant.STATIC_PHONE_NUMBER
import com.africell.africell.R
import com.africell.africell.app.viewbinding.BaseVBBottomSheetFragment
import com.africell.africell.app.viewbinding.withVBAvailable
import com.africell.africell.data.api.dto.BundleInfo
import com.africell.africell.data.api.requests.ActivateBundleRequest
import com.africell.africell.data.entity.Country
import com.africell.africell.data.repository.domain.SessionRepository
import com.africell.africell.databinding.FragmentActivateBundleBinding
import com.africell.africell.features.authentication.CountriesAdapter
import com.africell.africell.ui.hideKeyboard
import com.africell.africell.ui.spinner.MaterialSpinner
import com.africell.africell.ui.spinner.OnItemSelectedListener
import com.africell.africell.ui.viewmodel.observeResource
import com.africell.africell.ui.viewmodel.observeResourceInline
import com.africell.africell.ui.viewmodel.observeResourceWithoutProgress
import com.africell.africell.ui.viewmodel.provideViewModel
import com.africell.africell.util.getText
import com.africell.africell.util.setText
import com.africell.africell.util.validation.PhoneNumberHelper
import com.benitobertoli.liv.Liv
import com.benitobertoli.liv.rule.NotEmptyRule
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ActivateBundleFragment : BaseVBBottomSheetFragment<FragmentActivateBundleBinding>(), Liv.Action {
    private var liv: Liv? = null

    val isActivateForMe by lazy {
        arguments?.getBoolean(ACTIVATE_FOR_ME)
            ?: throw IllegalArgumentException("required active for me argument")
    }

    val bundle by lazy {
        arguments?.getParcelable<BundleInfo>(BUNDLE_DETAILS)
            ?: throw IllegalArgumentException("required bundle arguments")
    }
    val PERMISSIONS_REQUEST_PHONE_NUMBER = 102

    @Inject
    lateinit var sessionRepository: SessionRepository

    private val viewModel by provideViewModel<ActivateBundleViewModel>()

    companion object {
        const val BUNDLE_DETAILS = "bundle_details"
        const val ACTIVATE_FOR_ME = "activate_for_me"
        fun newInstance(bundle: BundleInfo, isActivateForMe: Boolean): ActivateBundleFragment {
            return ActivateBundleFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BUNDLE_DETAILS, bundle)
                    putBoolean(ACTIVATE_FOR_ME, isActivateForMe)
                }
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(container, FragmentActivateBundleBinding::inflate, true)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        liv = initLiv()
        liv?.start()

        withVBAvailable {
            submitBtn.setOnClickListener {
                activity?.hideKeyboard()
                liv?.submitWhenValid()
            }
        }
        bindData()

        setupUI()
    }

    fun visibilityAutoRenew() {
        withVBAvailable {
            if (isActivateForMe) {
                isAutoRenew.visibility = View.VISIBLE
                autoRenewVisibility()
                toNumberLayout.onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onItemSelected(parent: MaterialSpinner, view: View?, position: Int, id: Long) {
                        autoRenewVisibility()
                    }

                    override fun onNothingSelected(parent: MaterialSpinner) {
                    }

                }
                fromLayout.onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onItemSelected(parent: MaterialSpinner, view: View?, position: Int, id: Long) {
                        autoRenewVisibility()
                    }

                    override fun onNothingSelected(parent: MaterialSpinner) {
                    }
                }
            } else {
                hideAutoRenew()
            }
        }

    }

    private fun autoRenewVisibility() {
        withVBAvailable {
            if (toNumberLayout.getText() == fromLayout.getText()) {
                isAutoRenew.visibility = View.VISIBLE
            } else {
                hideAutoRenew()
            }
        }
    }

    private fun hideAutoRenew() {
        withVBAvailable {
            isAutoRenew.isChecked = false
            isAutoRenew.visibility = View.GONE
        }
    }

    private fun setupUI() {
        withVBAvailable {
            if (isActivateForMe) {
                toNumberLayout.visibility = View.VISIBLE
                toSomeOneElseLayout.visibility = View.GONE
                submitBtn.setBackgroundColor(resources.getColor(R.color.yellow))
            } else {
                toNumberLayout.visibility = View.GONE
                toSomeOneElseLayout.visibility = View.VISIBLE
                submitBtn.setBackgroundColor(resources.getColor(R.color.purple))
            }
            titleTxt.text = bundle.getTitle()
            volumeTxt.text = bundle.getFormatVolume()
            validityTxt.text = bundle.getFormatValidity()

            mobileNumberLayout.setEndIconOnClickListener {
                activity?.hideKeyboard()
                phoneNumberPermission()
            }
        }
    }

    private fun initLiv(): Liv {
        val notEmptyRule = NotEmptyRule()
        val builder = Liv.Builder()
        builder.add(requireBinding().fromLayout, notEmptyRule)
        if (isActivateForMe) {
            builder.add(requireBinding().toNumberLayout, notEmptyRule)
        } else builder.add(requireBinding().mobileNumberLayout, notEmptyRule)
        return builder
            .submitAction(this)
            .build()

    }

    private fun bindData() {
        viewModel.getSubAccounts()
        observeResourceInline(viewModel.subAccountData, {
            withVBAvailable {
                val arrayAdapter = ArrayAdapter(requireContext(), R.layout.textview_spinner, it)
                arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
                fromLayout.adapter = arrayAdapter
                toNumberLayout.adapter = arrayAdapter

                it.indexOfFirst { it.account == sessionRepository.selectedMsisdn }?.takeIf { it != -1 }?.let {
                    fromLayout.selection = it
                    toNumberLayout.selection = it
                }


                visibilityAutoRenew()
            }

        })
        viewModel.getCountries()
        observeResourceWithoutProgress(viewModel.countriesData, {
            withVBAvailable {
                countrySpinner.adapter = CountriesAdapter(requireContext(), it)
                it.indexOfFirst { it.phonecode == STATIC_PHONE_NUMBER }?.takeIf { it != -1 }?.let {
                    countrySpinner.selection = it
                }
                countrySpinner.isEnabled = false
            }
        })
        observeResource(viewModel.activateBundleData) {
            showMaterialMessageDialog(
                getString(R.string.successful),
                it.resultText.orEmpty(),
                getString(R.string.close)
            ) {
                this@ActivateBundleFragment.dismiss()
                // findNavController().popBackStack()
            }
        }

    }


    override fun performAction() {
        withVBAvailable {
            val toNumber = if (isActivateForMe) {
                toNumberLayout.getText()
            } else {
                val phoneCode = (countrySpinner.selectedItem as? Country)?.phonecode?.replace("+", "")
                PhoneNumberHelper.getFormattedIfValid("", phoneCode + mobileNumberLayout.getText())?.replace("+", "")
            }

            val request = ActivateBundleRequest(bundle.bundleId, isAutoRenew.isChecked, fromLayout.getText(), toNumber)
            viewModel.activateBundle(request)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        liv?.dispose()
    }

    private fun phoneNumberPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            startActivityForResult(intent, 1)
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), PERMISSIONS_REQUEST_PHONE_NUMBER)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_PHONE_NUMBER -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
                    startActivityForResult(intent, 1)
                } else {
                    requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), PERMISSIONS_REQUEST_PHONE_NUMBER)
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            data?.data?.let { contactData ->

                val c: Cursor? = requireActivity().contentResolver.query(contactData, null, null, null, null)
                c?.let { c ->

                    if (c.moveToFirst()) {
                        val id: String = c.getString(
                            c.getColumnIndex(ContactsContract.Contacts._ID)
                        )
                        //  val name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                        val hasPhone: Int = c.getInt(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                        if (hasPhone == 1) {
                            val pCur: Cursor? = requireActivity().contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", arrayOf(id), null
                            )
                            val allMobileNumber = mutableListOf<String>()
                            while (pCur?.moveToNext() == true) {
                                val number = pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER
                                ).let {
                                    pCur.getString(
                                        it
                                    )
                                }
                                if (number != null) {
                                    allMobileNumber.add(number)
                                }
                            }

                            pCur?.close()
                            selectPhoneNumber(allMobileNumber) { number ->
                                withVBAvailable {
                                    val phoneCode = (countrySpinner.selectedItem as? Country)?.phonecode
                                    val formatted = PhoneNumberHelper.getFormattedIfValid(phoneCode, number)
                                    formatted?.let {
                                        val pairNumber = PhoneNumberHelper.getCodeAndNumber(formatted)
                                        mobileNumberLayout.setText(pairNumber?.second ?: formatted)
                                    } ?: showMessage(getString(R.string.phone_number_not_valid))
                                }
                            }
                        }
                    }
                }
            }
        } else super.onActivityResult(requestCode, resultCode, data)
    }
}

