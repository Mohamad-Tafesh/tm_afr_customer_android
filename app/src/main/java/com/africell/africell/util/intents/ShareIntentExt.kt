@file:JvmName("ShareIntentUtils")

package com.africell.africell.util.intents

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.app.ShareCompat
import com.google.android.material.snackbar.Snackbar
import com.africell.africell.app.BaseActivity
import com.africell.africell.app.BaseFragment
import androidx.fragment.app.Fragment as SupportFragment

//Intents:

fun getShareIntent(subject: String, message: String) = Intent(Intent.ACTION_SEND).apply {
    putExtra(Intent.EXTRA_SUBJECT, subject)
    putExtra(Intent.EXTRA_TEXT, message)
    type = "text/plain"
}

fun Context.getShareIntent(@StringRes subjectRes: Int, message: String) = resources.getShareIntent(subjectRes, message)

fun Context.getShareIntent(subject: String, @StringRes messageRes: Int) = resources.getShareIntent(subject, messageRes)

fun Context.getShareIntent(@StringRes subjectRes: Int, @StringRes messageRes: Int) =
    resources.getShareIntent(subjectRes, messageRes)

fun SupportFragment.getShareIntent(@StringRes subjectRes: Int, message: String) =
    resources.getShareIntent(subjectRes, message)

fun SupportFragment.getShareIntent(subject: String, @StringRes messageRes: Int) =
    resources.getShareIntent(subject, messageRes)

fun SupportFragment.getShareIntent(@StringRes subjectRes: Int, @StringRes messageRes: Int) =
    resources.getShareIntent(subjectRes, messageRes)

fun Resources.getShareIntent(@StringRes subjectRes: Int, message: String) =
    getShareIntent(getString(subjectRes), message)

fun Resources.getShareIntent(subject: String, @StringRes messageRes: Int) =
    getShareIntent(subject, getString(messageRes))

fun Resources.getShareIntent(@StringRes subjectRes: Int, @StringRes messageRes: Int) =
    getShareIntent(getString(subjectRes), getString(messageRes))

//Activity:

fun Activity.share(
    @StringRes subjectRes: Int, @StringRes messageRes: Int, forceChooser: Boolean = false,
    chooserTitle: String = ""
) {
    startActivityIntent(getShareIntent(subjectRes, messageRes), forceChooser, chooserTitle)
}

fun Activity.share(
    subject: String, @StringRes messageRes: Int,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {
    startActivityIntent(getShareIntent(subject, messageRes), forceChooser, chooserTitle)
}

fun Activity.share(
    @StringRes subjectRes: Int, message: String,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {
    startActivityIntent(getShareIntent(subjectRes, message), forceChooser, chooserTitle)
}

fun Activity.share(subject: String, message: String, forceChooser: Boolean = false, chooserTitle: String = "") {
    startActivityIntent(getShareIntent(subject, message), forceChooser, chooserTitle)
}

fun Activity.openWhatsAppWith(phoneNumber: String) {
    /*try {
        val url = "https://api.whatsapp.com/send?phone=$phoneNumber"
        val i = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        startActivity(i)
    } catch (e: Exception) {
        Toast.makeText(this, "Phone number unavailable", Toast.LENGTH_LONG).show()
    }*/
    try {
        val url = "https://wa.me/$phoneNumber"
        startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                `package` = "com.whatsapp"
            }
        )
    } catch (e: Exception) {
        Snackbar.make(
            findViewById(android.R.id.content),
            "WhatsApp is not installed; please install WhatsApp",
            Snackbar.LENGTH_LONG
        ).show()
    }
}

fun BaseActivity.openWhatsAppWith(phoneNumber: String) {
    /*try {
        val url = "https://api.whatsapp.com/send?phone=$phoneNumber"
        val i = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        startActivity(i)
    } catch (e: Exception) {
        Toast.makeText(this, "Phone number unavailable", Toast.LENGTH_LONG).show()
    }*/
    try {
        val url = "https://wa.me/$phoneNumber"
        startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                `package` = "com.whatsapp"
            }
        )
    } catch (e: Exception) {
        showMessage("WhatsApp is not installed; please install WhatsApp")
    }
}

//...

//Support fragment:

fun SupportFragment.share(
    @StringRes subjectRes: Int, @StringRes messageRes: Int, forceChooser: Boolean = false,
    chooserTitle: String = ""
) {
    startActivityIntent(getShareIntent(subjectRes, messageRes), forceChooser, chooserTitle)
}

fun SupportFragment.share(
    subject: String, @StringRes messageRes: Int,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {
    startActivityIntent(getShareIntent(subject, messageRes), forceChooser, chooserTitle)
}

fun SupportFragment.share(
    @StringRes subjectRes: Int, message: String,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {
    startActivityIntent(getShareIntent(subjectRes, message), forceChooser, chooserTitle)
}

fun SupportFragment.share(subject: String, message: String, forceChooser: Boolean = false, chooserTitle: String = "") {
    startActivityIntent(getShareIntent(subject, message), forceChooser, chooserTitle)
}

fun SupportFragment.openWhatsAppWith(phoneNumber: String) {
    /*try {
        val url = "https://api.whatsapp.com/send?phone=$phoneNumber"
        val i = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        startActivity(i)
    } catch (e: Exception) {
        Toast.makeText(this, "Phone number unavailable", Toast.LENGTH_LONG).show()
    }*/
    try {
        val url = "https://wa.me/$phoneNumber"
        startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                `package` = "com.whatsapp"
            }
        )
    } catch (e: Exception) {
        view?.let { view ->
            Snackbar.make(view, "WhatsApp is not installed; please install WhatsApp", Snackbar.LENGTH_LONG).show()
        }
    }
}

fun BaseFragment.openWhatsAppWith(phoneNumber: String) {
    /*try {
        val url = "https://api.whatsapp.com/send?phone=$phoneNumber"
        val i = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        startActivity(i)
    } catch (e: Exception) {
        Toast.makeText(this, "Phone number unavailable", Toast.LENGTH_LONG).show()
    }*/
    try {
        val url = "https://wa.me/$phoneNumber"
        startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                `package` = "com.whatsapp"
            }
        )
    } catch (e: Exception) {
        showMessage("WhatsApp is not installed; please install WhatsApp")
    }
}

//...

//Context:

fun Context.share(
    @StringRes subjectRes: Int, @StringRes messageRes: Int, forceChooser: Boolean = false,
    chooserTitle: String = ""
) {
    startActivityIntent(getShareIntent(subjectRes, messageRes), forceChooser, chooserTitle)
}

fun Context.share(
    subject: String, @StringRes messageRes: Int,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {
    startActivityIntent(getShareIntent(subject, messageRes), forceChooser, chooserTitle)
}

fun Context.share(
    @StringRes subjectRes: Int, message: String,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {
    startActivityIntent(getShareIntent(subjectRes, message), forceChooser, chooserTitle)
}

fun Context.share(subject: String, message: String, forceChooser: Boolean = false, chooserTitle: String = "") {
    startActivityIntent(getShareIntent(subject, message), forceChooser, chooserTitle)
}

fun Context.openWhatsAppWith(phoneNumber: String) {
    /*try {
        val url = "https://api.whatsapp.com/send?phone=$phoneNumber"
        val i = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        startActivity(i)
    } catch (e: Exception) {
        Toast.makeText(this, "Phone number unavailable", Toast.LENGTH_LONG).show()
    }*/
    try {
        val url = "https://wa.me/$phoneNumber"
        startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                `package` = "com.whatsapp"
            }
        )
    } catch (e: Exception) {
        Toast.makeText(this, "WhatsApp is not installed; please install WhatsApp", Toast.LENGTH_LONG).show()
    }
}

//...


fun Activity.shareCompat(
    subject: String,
    message: String,
    toEmail: String? = null,
    ccEmail: String? = null,
    bccEmail: String? = null,
    streamUri: Uri? = null,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {

    startActivity(
        getShareCompatIntent(
            subject,
            message,
            toEmail,
            ccEmail,
            bccEmail,
            streamUri,
            forceChooser,
            chooserTitle
        )
    )
}

fun Activity.shareCompat(
    subject: String,
    message: String,
    toEmails: Array<String>? = null,
    ccEmails: Array<String>? = null,
    bccEmails: Array<String>? = null,
    streamUri: Uri? = null,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {

    startActivity(
        getShareCompatIntent(
            subject,
            message,
            toEmails,
            ccEmails,
            bccEmails,
            streamUri,
            forceChooser,
            chooserTitle
        )
    )
}

fun Activity.getShareCompatIntent(
    subject: String,
    message: String,
    toEmail: String? = null,
    ccEmail: String? = null,
    bccEmail: String? = null,
    streamUri: Uri? = null,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
): Intent =

    getShareCompatIntent(
        subject,
        message,
        toEmail?.let { arrayOf(it) },
        ccEmail?.let { arrayOf(it) },
        bccEmail?.let { arrayOf(it) },
        streamUri,
        forceChooser,
        chooserTitle
    )

fun Activity.getShareCompatIntent(
    subject: String,
    message: String,
    toEmails: Array<String>? = null,
    ccEmails: Array<String>? = null,
    bccEmails: Array<String>? = null,
    streamUri: Uri? = null,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
): Intent {

    val builder = ShareCompat.IntentBuilder.from(this)
        .setSubject(subject)
        .setText(message)
        .setChooserTitle(chooserTitle)
    toEmails?.let { builder.addEmailTo(it) }
    ccEmails?.let { builder.addEmailCc(it) }
    bccEmails?.let { builder.addEmailBcc(it) }
    streamUri?.let { builder.addStream(streamUri) }

    return if (forceChooser) {
        builder.createChooserIntent()
    } else {
        builder.intent
    }
}