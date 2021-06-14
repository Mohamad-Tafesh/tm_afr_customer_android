package com.tedmob.africell.features.sms

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.benitobertoli.liv.Liv
import com.benitobertoli.liv.rule.NotEmptyRule
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.data.api.ApiContract
import com.tedmob.africell.data.entity.Country
import com.tedmob.africell.data.repository.domain.SessionRepository
import com.tedmob.africell.features.authentication.CountriesAdapter
import com.tedmob.africell.ui.hideKeyboard
import com.tedmob.africell.ui.viewmodel.observeResource
import com.tedmob.africell.ui.viewmodel.observeResourceInline
import com.tedmob.africell.ui.viewmodel.provideViewModel
import com.tedmob.africell.util.getText
import com.tedmob.africell.util.setText
import com.tedmob.africell.util.validation.PhoneNumberHelper
import kotlinx.android.synthetic.main.fragment_credit_transfer.*
import kotlinx.android.synthetic.main.fragment_sms.*
import kotlinx.android.synthetic.main.fragment_sms.countrySpinner
import kotlinx.android.synthetic.main.fragment_sms.mobileNumberLayout
import kotlinx.android.synthetic.main.fragment_sms.sendBtn
import javax.inject.Inject


class SMSFragment : BaseFragment(), Liv.Action {
    private var liv: Liv? = null


    private val viewModel by provideViewModel<SMSViewModel> { viewModelFactory }
    val PERMISSIONS_REQUEST_PHONE_NUMBER = 102

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_sms, R.layout.toolbar_default, true)
    }

    @Inject
    lateinit var sessionRepository: SessionRepository
    override fun configureToolbar() {
        super.configureToolbar()

        actionbar?.title = getString(R.string.send_free_sms)
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_side_menu)
        actionbar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (sessionRepository.isLoggedIn()) {
            liv = initLiv()
            liv?.start()
            viewModel.getSmsCount()
            sendBtn.setOnClickListener {
                activity?.hideKeyboard()
                liv?.submitWhenValid()
            }
            mobileNumberLayout.setEndIconOnClickListener {
                activity?.hideKeyboard()
                phoneNumberPermission()
            }
        } else {
            showInlineMessageWithAction(getString(R.string.login_first), actionName = getString(R.string.login)) {
                redirectToLogin()
            }
        }

        bindData()

    }


    private fun initLiv(): Liv {
        val notEmptyRule = NotEmptyRule()

        return Liv.Builder()
            .add(mobileNumberLayout, notEmptyRule)
            .add(messageLayout, notEmptyRule)
            .submitAction(this)
            .build()

    }

    private fun bindData() {
        observeResourceInline(viewModel.smsData) {

            recyclerView.adapter = SMSAdapter(it.smsCount.smsCount ?: 0)
            val countries = it.countries
            countrySpinner.adapter = CountriesAdapter(requireContext(), countries)
            countries.indexOfFirst { it.phonecode == ApiContract.Params.SL_PHONE_NUMBER  }?.takeIf { it != -1 }?.let {
                countrySpinner.selection = it
            }
            countrySpinner.isEnabled = false
        }

        observeResource(viewModel.smsSentData) {
            showMessageDialog(it.resultText.orEmpty(), getString(R.string.close)) {
                recyclerView.adapter = SMSAdapter(it.smsCount ?: 0)
                liv?.dispose()
                messageLayout.setText("")
                mobileNumberLayout.setText("")
                liv?.start()
            }
        }
    }


    override fun performAction() {
        val phoneCode = (countrySpinner.selectedItem as? Country)?.phonecode?.replace("+", "")
        val formatted =
            PhoneNumberHelper.getFormattedIfValid(phoneCode, mobileNumberLayout.getText())
                ?.replace("+", "")
        formatted?.let {
            viewModel.sendSMS(it, messageLayout.getText())
        } ?: showMessage(getString(R.string.phone_number_not_valid))

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


    override fun onDestroyView() {
        super.onDestroyView()
        liv?.dispose()
    }

}

