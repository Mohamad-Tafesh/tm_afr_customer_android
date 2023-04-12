@file:JvmName("WebIntentUtils")

package com.tedmob.afrimoney.util.intents

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment as SupportFragment

//Activity:

fun Activity.openWebsite(@StringRes urlRes: Int, forceChooser: Boolean = false, chooserTitle: String = "") {
    startActivityIntent(getWebsiteViewIntent(urlRes), forceChooser, chooserTitle)
}

fun Activity.openWebsite(url: String, forceChooser: Boolean = false, chooserTitle: String = "") {
    startActivityIntent(getWebsiteViewIntent(url), forceChooser, chooserTitle)
}

fun Activity.openWebsite(
    url: String?,
    @StringRes missingMessageRes: Int,
    anchorView: View = window.decorView,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {

    openWebsite(url, getString(missingMessageRes), anchorView, forceChooser, chooserTitle)
}

fun Activity.openWebsite(
    url: String?,
    missingMessage: String,
    anchorView: View = window.decorView,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {

    startActivityIntent(url?.let { getWebsiteViewIntent(url) }, missingMessage, anchorView, forceChooser, chooserTitle)
}

//...

//Support fragment:

fun SupportFragment.openWebsite(@StringRes urlRes: Int, forceChooser: Boolean = false, chooserTitle: String = "") {
    startActivityIntent(getWebsiteViewIntent(urlRes), forceChooser, chooserTitle)
}

fun SupportFragment.openWebsite(url: String, forceChooser: Boolean = false, chooserTitle: String = "") {
    startActivityIntent(getWebsiteViewIntent(url), forceChooser, chooserTitle)
}

fun SupportFragment.openWebsite(
    url: String?,
    @StringRes missingMessageRes: Int,
    anchorView: View? = view ?: activity?.window?.decorView,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {

    openWebsite(url, getString(missingMessageRes), anchorView, forceChooser, chooserTitle)
}

fun SupportFragment.openWebsite(
    url: String?,
    missingMessage: String,
    anchorView: View? = view ?: activity?.window?.decorView,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {

    startActivityIntent(url?.let { getWebsiteViewIntent(url) }, missingMessage, anchorView, forceChooser, chooserTitle)
}

//...

//Context:

fun Context.openWebsite(@StringRes urlRes: Int, forceChooser: Boolean = false, chooserTitle: String = "") {
    startActivityIntent(getWebsiteViewIntent(urlRes), forceChooser, chooserTitle)
}

fun Context.openWebsite(url: String, forceChooser: Boolean = false, chooserTitle: String = "") {
    startActivityIntent(getWebsiteViewIntent(url), forceChooser, chooserTitle)
}

fun Context.openWebsite(
    url: String?,
    @StringRes missingMessageRes: Int,
    anchorView: View? = null,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {

    openWebsite(url, getString(missingMessageRes), anchorView, forceChooser, chooserTitle)
}

fun Context.openWebsite(
    url: String?,
    missingMessage: String,
    anchorView: View? = null,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {

    startActivityIntent(url?.let { getWebsiteViewIntent(url) }, missingMessage, anchorView, forceChooser, chooserTitle)
}
