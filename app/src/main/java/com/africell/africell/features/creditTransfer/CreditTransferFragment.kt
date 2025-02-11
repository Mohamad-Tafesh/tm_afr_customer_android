package com.africell.africell.features.creditTransfer

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
import androidx.navigation.fragment.findNavController
import com.africell.africell.Constant.STATIC_PHONE_NUMBER
import com.africell.africell.R
import com.africell.africell.app.viewbinding.BaseVBFragment
import com.africell.africell.app.viewbinding.withVBAvailable
import com.africell.africell.data.api.ApiContract
import com.africell.africell.data.entity.Country
import com.africell.africell.data.repository.domain.SessionRepository
import com.africell.africell.databinding.FragmentCreditTransferBinding
import com.africell.africell.databinding.ToolbarDefaultBinding
import com.africell.africell.features.authentication.CountriesAdapter
import com.africell.africell.ui.hideKeyboard
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
class CreditTransferFragment : BaseVBFragment<FragmentCreditTransferBinding>(), Liv.Action {
    private var liv: Liv? = null


    private val viewModel by provideViewModel<CreditTransferViewModel>()
    val PERMISSIONS_REQUEST_PHONE_NUMBER = 102

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(
            container,
            FragmentCreditTransferBinding::inflate,
            true,
            ToolbarDefaultBinding::inflate
        )
    }

    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.title = getString(R.string.credit_transfer)
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_back)
        actionbar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
    }

    @Inject
    lateinit var sessionRepository: SessionRepository
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        withVBAvailable {
            setupImageBanner(imageView, ApiContract.Params.BANNERS, ApiContract.ImagePageName.CREDIT_TRANSFER)
            liv = initLiv()
            liv?.start()
            sendBtn.setOnClickListener { liv?.submitWhenValid() }
            bindData()
            bindCountries()
            mobileNumberLayout.setEndIconOnClickListener {
                activity?.hideKeyboard()
                phoneNumberPermission()
            }
        }
    }

    private fun FragmentCreditTransferBinding.initLiv(): Liv {
        val notEmptyRule = NotEmptyRule()

        return Liv.Builder()
            .add(mobileNumberLayout, notEmptyRule)
            .add(amountLayout, notEmptyRule)
            .submitAction(this@CreditTransferFragment)
            .build()

    }

    private fun bindData() {
        /*   observeResourceInline(viewModel.cardsData) {
               recyclerView.adapter = adapter
               adapter.setItems(it)
           }
   */
        viewModel.getSubAccounts()
        observeResourceInline(viewModel.subAccountData) {
            withVBAvailable {
                val arrayAdapter = ArrayAdapter(requireContext(), R.layout.textview_spinner, it)
                arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
                fromNumberLayout.adapter = arrayAdapter
                it.indexOfFirst { it.account == sessionRepository.selectedMsisdn }?.takeIf { it != -1 }?.let {
                    fromNumberLayout.selection = it
                }
            }

        }
        observeResource(viewModel.creditTransferData) {
            showMaterialMessageDialog(
                getString(R.string.successful),
                it.resultText.orEmpty(),
                getString(R.string.close)
            ) {
                /*   liv?.dispose()
                   amountLayout.setText("")
                   mobileNumberLayout.setText("")
                   liv?.start()*/
                findNavController().popBackStack()
            }
        }
    }

    private fun bindCountries() {
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
    }

    override fun performAction() {
        withVBAvailable {
            val phoneCode = (countrySpinner.selectedItem as? Country)?.phonecode
            val formatted =
                PhoneNumberHelper.getFormattedIfValid(phoneCode, mobileNumberLayout.getText())?.replace("+", "")
            formatted?.let {
                viewModel.creditTransfer(fromNumberLayout.getText(), it, amountLayout.getText())
            } ?: showMessage(getString(R.string.phone_number_not_valid))
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

