@file:JvmName("OpenIntentUtils")

package com.africell.africell.util.intents

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.Fragment as SupportFragment

//Activity:

fun Activity.startActivityIntent(intent: Intent, forceChooser: Boolean = false, chooserTitle: String? = null) {
    startActivity(if (forceChooser) Intent.createChooser(intent, chooserTitle) else intent)
}

fun Activity.startActivityIntent(
    intent: Intent?,
    missingMessage: String?,
    anchorView: View = window.decorView,
    forceChooser: Boolean = false,
    chooserTitle: String? = null
) {

    if (intent != null) {
        startActivityIntent(intent, forceChooser, chooserTitle)
    } else if (missingMessage != null) {
        showMissingMessage(anchorView, missingMessage)
    }
}

//...

//Support fragment:

fun SupportFragment.startActivityIntent(intent: Intent, forceChooser: Boolean = false, chooserTitle: String? = null) {
    startActivity(if (forceChooser) Intent.createChooser(intent, chooserTitle) else intent)
}

fun SupportFragment.startActivityIntent(
    intent: Intent?,
    missingMessage: String?,
    anchorView: View? = view ?: activity?.window?.decorView,
    forceChooser: Boolean = false,
    chooserTitle: String? = null
) {

    if (intent != null) {
        startActivityIntent(intent, forceChooser, chooserTitle)
    } else if (anchorView != null && missingMessage != null) {
        showMissingMessage(anchorView, missingMessage)
    }
}

//...

//Context:

fun Context.startActivityIntent(intent: Intent, forceChooser: Boolean = false, chooserTitle: String? = null) {
    startActivity(if (forceChooser) Intent.createChooser(intent, chooserTitle) else intent)
}

fun Context.startActivityIntent(
    intent: Intent?,
    missingMessage: String?,
    anchorView: View? = null,
    forceChooser: Boolean = false,
    chooserTitle: String? = null
) {

    if (intent != null) {
        startActivityIntent(intent, forceChooser, chooserTitle)
    } else if (anchorView != null && missingMessage != null) {
        showMissingMessage(anchorView, missingMessage)
    }
}

//...

private fun showMissingMessage(anchorView: View, missingMessage: String) {
    Snackbar.make(anchorView, missingMessage, Snackbar.LENGTH_LONG).show()
}