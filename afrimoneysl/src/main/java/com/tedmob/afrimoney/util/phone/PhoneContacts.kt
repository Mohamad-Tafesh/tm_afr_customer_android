package com.tedmob.afrimoney.util.phone

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.ContactsContract
import androidx.core.app.ActivityCompat
import androidx.viewbinding.ViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBFragment
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

abstract class BaseVBFragmentWithImportContact<VB : ViewBinding> : BaseVBFragment<VB>() {

    companion object {
        private val REQUEST__CONTACTS = 100
        private val RESULT__CONTACTS = 200
    }

    @Inject
    lateinit var phoneHelper: PhoneNumberHelper

    private var permissionContinuation: CancellableContinuation<Boolean>? = null
    private var importContactContinuation: CancellableContinuation<String?>? = null
    private var defaultCountryCode: String? = null


    protected suspend fun importContact(defaultCountryCode: String? = null): String? {
        val isAllowed = suspendCancellableCoroutine<Boolean> {
            permissionContinuation = it
            checkImportContactPermissions()
        }

        return if (isAllowed) {
            suspendCancellableCoroutine {
                importContactContinuation = it
                this.defaultCountryCode = defaultCountryCode
                importContactAction()
            }
        } else {
            null
        }
    }

    private fun checkImportContactPermissions() {
        if (
            ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_CONTACTS) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            permissionContinuation?.resume(true)
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST__CONTACTS)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST__CONTACTS -> if (grantResults.getOrNull(0) == PackageManager.PERMISSION_GRANTED) {
                permissionContinuation?.resume(true)
            } else {
                showReadContactNotAllowed()
                permissionContinuation?.resume(false)
            }

            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private inline fun showReadContactNotAllowed() {
        MaterialAlertDialogBuilder(requireActivity())
            .setMessage(R.string.contact_permission_refused)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    private inline fun importContactAction() {
        startActivityForResult(
            Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI),
            RESULT__CONTACTS
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            RESULT__CONTACTS -> {
                if (resultCode == Activity.RESULT_OK) {
                    val mobileNumber = data?.data?.extractContactInfo()
                    importContactContinuation?.resume(mobileNumber)
                }
            }

            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun Uri.extractContactInfo(): String? {
        return requireActivity().contentResolver
            .query(this, null, null, null, null)
            .use { cursor ->
                var phoneNumber: String? = null

                if (cursor != null && cursor.moveToFirst()) {
                    val phoneNumberRaw =
                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            .let { cursor.getString(it) }
                            .orEmpty()

                    phoneNumber = phoneNumberRaw.replace("\\s+".toRegex(), "")
                }

                cursor?.close()

                phoneNumber?.let {
                    val data = phoneHelper.getCodeAndNumber(phoneNumber, "+220")
                    if (data != null) {
                        //number has the country code included
                        //phoneNumber
                        data.second
                    } else {
                        //number does not have the country code included
                        //"${defaultCountryCode.orEmpty()}$phoneNumber"
                        phoneNumber
                    }
                }
            }
    }


    override fun onDestroy() {
        super.onDestroy()
        importContactContinuation?.cancel()
        permissionContinuation?.cancel()
    }
}