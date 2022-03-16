package com.africell.africell.features.afrimoneyActivateBundle

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
import androidx.navigation.fragment.findNavController
import com.africell.africell.Constant
import com.africell.africell.R
import com.africell.africell.app.BaseFragment
import com.africell.africell.data.api.dto.BundleInfo
import com.africell.africell.data.api.dto.WalletDTO
import com.africell.africell.data.api.requests.afrimoney.AfrimoneyActivateBundleRequest
import com.africell.africell.data.entity.Country
import com.africell.africell.data.repository.domain.SessionRepository
import com.africell.africell.ui.hideKeyboard
import com.africell.africell.ui.viewmodel.observeResource
import com.africell.africell.ui.viewmodel.observeResourceInline
import com.africell.africell.ui.viewmodel.provideViewModel
import com.africell.africell.util.getText
import com.africell.africell.util.setText
import com.africell.africell.util.validation.PhoneNumberHelper
import com.benitobertoli.liv.Liv
import com.benitobertoli.liv.rule.NotEmptyRule
import kotlinx.android.synthetic.main.fragment_afrimoney_activate_bundle.*
import javax.inject.Inject


class AfrimoneyActivateBundleFragment : BaseFragment(), Liv.Action {
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

    private val viewModel by provideViewModel<AfrimoneyActivateBundleViewModel> { viewModelFactory }

    companion object {
        const val BUNDLE_DETAILS = "bundle_details"
        const val ACTIVATE_FOR_ME = "activate_for_me"
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_afrimoney_activate_bundle, 0, true)
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


    private fun setupUI() {
        if (isActivateForMe) {
            toSomeOneElseLayout.visibility = View.GONE
            submitBtn.setBackgroundColor(resources.getColor(R.color.yellow))
        } else {
            toSomeOneElseLayout.visibility = View.VISIBLE
            submitBtn.setBackgroundColor(resources.getColor(R.color.purple))
        }
        titleTxt.text =
            if (isActivateForMe) getString(R.string.activate_for_me) else getString(R.string.activate_bundle_for_someone_else)
        closeIcon.setOnClickListener {
            findNavController().popBackStack()
        }
        mobileNumberLayout.setEndIconOnClickListener {
            activity?.hideKeyboard()
            phoneNumberPermission()
        }
    }

    private fun initLiv(): Liv {
        val notEmptyRule = NotEmptyRule()
        val builder = Liv.Builder()
        builder.add(selectWalletLayout, notEmptyRule)
        return builder
            .submitAction(this)
            .build()

    }

    private fun bindData() {
        viewModel.getData()
        observeResourceInline(viewModel.data, {
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.textview_spinner, it)
            arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            selectWalletLayout.adapter = arrayAdapter


        })
        observeResource(viewModel.requestData) {
            showMaterialMessageDialog(getString(R.string.successful),it.resultText.orEmpty(), getString(R.string.close)) {
                findNavController().popBackStack()
            }
        }

    }


    override fun performAction() {

        val wallet = (selectWalletLayout.selectedItem as? WalletDTO)?.name
        // PhoneNumberHelper.getFormattedIfValid("",  mobileNumberLayout.getText())?.replace("+", "")
        val subMsisdn = if (sessionRepository.selectedMsisdn != sessionRepository.msisdn) sessionRepository.selectedMsisdn else null

        val request = AfrimoneyActivateBundleRequest(
            bundle.bundleId?.toString().orEmpty(),
            wallet,
            subMsisdn,
            mobileNumberLayout.getText(),
            pinCodeLayout.getText()
        )
        viewModel.submitRequest(request)
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
                            val allMobileNumber = mutableListOf<String>()
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
                                val phoneCode = Constant.STATIC_PHONE_NUMBER
                                val formatted = PhoneNumberHelper.getFormattedIfValid(phoneCode, number)?.replace("+","")
                                formatted?.let {
                                    //val pairNumber = PhoneNumberHelper.getCodeAndNumber(formatted)
                                    mobileNumberLayout.setText(formatted)
                                } ?: showMessage(getString(R.string.phone_number_not_valid))

                            }
                        }
                    }
                }
            }
        } else super.onActivityResult(requestCode, resultCode, data)
    }

}

