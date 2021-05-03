package com.tedmob.africell.features.activateBundle

import android.Manifest
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
import com.benitobertoli.liv.Liv
import com.benitobertoli.liv.rule.NotEmptyRule
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseBottomSheetFragment
import com.tedmob.africell.data.api.dto.BundleInfo
import com.tedmob.africell.data.api.requests.ActivateBundleRequest
import com.tedmob.africell.data.entity.Country
import com.tedmob.africell.data.repository.domain.SessionRepository
import com.tedmob.africell.features.authentication.CountriesAdapter
import com.tedmob.africell.features.bundles.BundleDetailsFragment
import com.tedmob.africell.features.bundles.BundleDetailsFragment.Companion.BUNDLE_DETAILS
import com.tedmob.africell.ui.hideKeyboard
import com.tedmob.africell.ui.spinner.MaterialSpinner
import com.tedmob.africell.ui.spinner.OnItemSelectedListener
import com.tedmob.africell.ui.viewmodel.observeResource
import com.tedmob.africell.ui.viewmodel.observeResourceInline
import com.tedmob.africell.ui.viewmodel.observeResourceWithoutProgress
import com.tedmob.africell.ui.viewmodel.provideViewModel
import com.tedmob.africell.util.getText
import com.tedmob.africell.util.setText
import com.tedmob.africell.util.validation.PhoneNumberHelper
import kotlinx.android.synthetic.main.fragment_activate_bundle.*
import kotlinx.android.synthetic.main.fragment_activate_bundle.countrySpinner
import kotlinx.android.synthetic.main.fragment_activate_bundle.mobileNumberLayout
import kotlinx.android.synthetic.main.fragment_credit_transfer.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_sms.*
import javax.inject.Inject


class ActivateBundleFragment : BaseBottomSheetFragment(), Liv.Action {
    private var liv: Liv? = null

    val isActivateForMe by lazy {
        arguments?.getBoolean(ACTIVATE_FOR_ME)
            ?: throw IllegalArgumentException("required active for me argument")
    }

    val bundle by lazy {
        arguments?.getParcelable<BundleInfo>(BundleDetailsFragment.BUNDLE_DETAILS)
            ?: throw IllegalArgumentException("required bundle arguments")
    }
    val PERMISSIONS_REQUEST_PHONE_NUMBER = 102

    @Inject
    lateinit var sessionRepository: SessionRepository

    private val viewModel by provideViewModel<ActivateBundleViewModel> { viewModelFactory }

    companion object {
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
        return wrap(inflater.context, R.layout.fragment_activate_bundle, 0, true)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        liv = initLiv()
        liv?.start()
        submitBtn.setOnClickListener {
            activity?.hideKeyboard()
            liv?.submitWhenValid()
        }
        bindData()

        setupUI()
    }

    fun visibilityAutoRenew() {
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

    private fun autoRenewVisibility() {
        if (toNumberLayout.getText() == fromLayout.getText()) {
            isAutoRenew.visibility = View.VISIBLE
        } else {
            hideAutoRenew()
        }
    }

    private fun hideAutoRenew() {
        isAutoRenew.isChecked = false
        isAutoRenew.visibility = View.GONE
    }

    private fun setupUI() {
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

    private fun initLiv(): Liv {
        val notEmptyRule = NotEmptyRule()
        val builder = Liv.Builder()
        builder.add(fromLayout, notEmptyRule)
        if (isActivateForMe) {
            builder.add(toNumberLayout, notEmptyRule)
        } else builder.add(mobileNumberLayout, notEmptyRule)
        return builder
            .submitAction(this)
            .build()

    }

    private fun bindData() {
        viewModel.getSubAccounts()
        observeResourceInline(viewModel.subAccountData, {
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.textview_spinner, it)
            arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            fromLayout.adapter = arrayAdapter
            toNumberLayout.adapter = arrayAdapter

            it.indexOfFirst { it.account == sessionRepository.selectedMsisdn }?.takeIf { it != -1 }?.let {
                fromLayout.selection = it
                toNumberLayout.selection = it
            }


            visibilityAutoRenew()

        })
        viewModel.getCountries()
        observeResourceWithoutProgress(viewModel.countriesData, {
            countrySpinner.adapter = CountriesAdapter(requireContext(), it)
            it.indexOfFirst { it.phonecode == "+220" }?.takeIf { it != -1 }?.let {
                countrySpinner.selection = it
            }
            countrySpinner.isEnabled=false

        })
        observeResource(viewModel.activateBundleData) {
            showMessageDialog(it.resultText.orEmpty(), getString(R.string.close)) {
                this.dismiss()
                // findNavController().popBackStack()
            }
        }

    }


    override fun performAction() {
        val toNumber = if (isActivateForMe) {
            toNumberLayout.getText()
        } else {
            val phoneCode = (countrySpinner.selectedItem as? Country)?.phonecode?.replace("+", "")
            PhoneNumberHelper.getFormattedIfValid("", phoneCode + mobileNumberLayout.getText())?.replace("+", "")
        }

        val request = ActivateBundleRequest(bundle.bundleId, isAutoRenew.isChecked, fromLayout.getText(), toNumber)
        viewModel.activateBundle(request)
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
                            val allMobileNumber= mutableListOf<String>()
                            while (pCur?.moveToNext() == true) {
                                val number = pCur?.getString(
                                    pCur?.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER
                                    )
                                )
                                allMobileNumber.add(number)
                            }

                            pCur?.close()
                            selectPhoneNumber(allMobileNumber) { number ->
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
        } else super.onActivityResult(requestCode, resultCode, data)
    }
}

