package com.africell.africell.features.lineRecharge

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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.benitobertoli.liv.Liv
import com.benitobertoli.liv.rule.NotEmptyRule
import com.africell.africell.R
import com.africell.africell.app.BaseFragment
import com.africell.africell.data.api.dto.RechargeCardDTO
import com.africell.africell.data.entity.Country
import com.africell.africell.ui.hideKeyboard
import com.africell.africell.ui.viewmodel.*
import com.africell.africell.util.getText
import com.africell.africell.util.setText
import com.africell.africell.util.validation.PhoneNumberHelper
import kotlinx.android.synthetic.main.fragment_line_recharge.*
import kotlinx.android.synthetic.main.fragment_line_recharge.countrySpinner
import kotlinx.android.synthetic.main.fragment_line_recharge.mobileNumberLayout
import kotlinx.android.synthetic.main.fragment_line_recharge.recyclerView
import kotlinx.android.synthetic.main.fragment_line_recharge.sendBtn


class LineRechargeFragment : BaseFragment(), Liv.Action {
    private var liv: Liv? = null


    private val viewModel by provideViewModel<LineRechargeViewModel> { viewModelFactory }
    val adapter by lazy {
        LineRechargeAdapter(mutableListOf(), object : LineRechargeAdapter.Callback {
            override fun onItemClickListener(item: RechargeCardDTO) {
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_line_recharge, R.layout.toolbar_default, true)
    }

    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.title = getString(R.string.line_recharge)
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_back)
        actionbar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        liv = initLiv()
        liv?.start()
        viewModel.getRechargeCards()
        sendBtn.setOnClickListener {
            activity?.hideKeyboard()
            liv?.submitWhenValid()
        }
        mobileNumberLayout.setEndIconOnClickListener {
            activity?.hideKeyboard()
            phoneNumberPermission()
        }
        bindData()
    }

    private fun initLiv(): Liv {
        val notEmptyRule = NotEmptyRule()

        return Liv.Builder()
            .add(mobileNumberLayout, notEmptyRule)
            .add(rechargeCardLayout, notEmptyRule)
            .submitAction(this)
            .build()

    }

    private fun bindData() {

        observeResourceInline(viewModel.cardsData) {
            val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.separator)
            drawable?.let {
                dividerItemDecoration.setDrawable(it)
            }
            recyclerView.addItemDecoration(dividerItemDecoration)
            recyclerView.adapter = adapter
            adapter.setItems(it)
        }

        observeResource(viewModel.rechargeVoucherData) {
            showMaterialMessageDialog(getString(R.string.successful),it.resultText.orEmpty(), getString(R.string.close)) {
                liv?.dispose()
                rechargeCardLayout.setText("")
                mobileNumberLayout.setText("")
                liv?.start()
            }
        }
    }


    override fun performAction() {
        val phoneCode = (countrySpinner.selectedItem as? Country)?.phonecode?.replace("+","")
        val formatted = PhoneNumberHelper.getFormattedIfValid("",phoneCode + mobileNumberLayout.getText())?.replace("+", "")

        formatted?.let {
            viewModel.rechargeVoucher(it, rechargeCardLayout.getText())
        } ?: showMessage(getString(R.string.phone_number_not_valid))

    }
    val PERMISSIONS_REQUEST_PHONE_NUMBER = 102

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
        }else super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        liv?.dispose()
    }
}

