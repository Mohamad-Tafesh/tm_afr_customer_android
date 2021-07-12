@file:JvmName("VideoIntentUtils")

package com.africell.africell.util.intents

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.view.View
import androidx.annotation.StringRes
import java.io.File
import androidx.fragment.app.Fragment as SupportFragment


fun getWatchVideoIntent(url: String): Intent = getViewIntentWithType(url, "video/*")

fun Resources.getWatchVideoIntent(@StringRes urlRes: Int): Intent = getWatchVideoIntent(getString(urlRes))

fun Context.getWatchVideoIntent(@StringRes urlRes: Int): Intent = resources.getWatchVideoIntent(urlRes)

fun SupportFragment.getWatchVideoIntent(@StringRes urlRes: Int): Intent = resources.getWatchVideoIntent(urlRes)


fun getWatchVideoIntent(file: File): Intent = getViewIntentWithType(file, "video/*")


fun getWatchVideoIntentFromProvider(
    context: Context,
    file: File,
    providerName: String = "${context.packageName}.provider"
): Intent =
    getViewIntentWithTypeFromProvider(context, file, "video/*", providerName)

fun Context.getWatchVideoIntentWithProvider(file: File, providerName: String = "$packageName.provider"): Intent =
    getViewIntentWithTypeFromProvider(this, file, "video/*", providerName)

fun SupportFragment.getWatchVideoIntentWithProvider(
    file: File,
    providerName: String = "${activity?.packageName}.provider"
): Intent =
    getViewIntentWithTypeFromProvider(
        requireNotNull(activity) { "Activity of support fragment is null." },
        file,
        "video/*",
        providerName
    )

//Activity:

//Url:

fun Activity.watchVideo(@StringRes urlRes: Int, forceChooser: Boolean = false, chooserTitle: String = "") {
    watchVideo(getString(urlRes), forceChooser, chooserTitle)
}

fun Activity.watchVideo(url: String, forceChooser: Boolean = false, chooserTitle: String = "") {
    startActivityIntent(getWatchVideoIntent(url), forceChooser, chooserTitle)
}

fun Activity.watchVideo(
    url: String?,
    @StringRes missingMessageRes: Int,
    anchorView: View = window.decorView,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {

    watchVideo(url, getString(missingMessageRes), anchorView, forceChooser, chooserTitle)
}

fun Activity.watchVideo(
    url: String?,
    missingMessage: String?,
    anchorView: View = window.decorView,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {

    startActivityIntent(url?.let { getWatchVideoIntent(it) }, missingMessage, anchorView, forceChooser, chooserTitle)
}

//File:

fun Activity.watchVideo(file: File, forceChooser: Boolean = false, chooserTitle: String = "") {
    startActivityIntent(getWatchVideoIntent(file), forceChooser, chooserTitle)
}

fun Activity.watchVideoFromProvider(
    file: File,
    forceChooser: Boolean = false,
    chooserTitle: String = "",
    providerName: String = "$packageName.provider"
) {

    startActivityIntent(getWatchVideoIntentWithProvider(file, providerName), forceChooser, chooserTitle)
}

fun Activity.watchVideo(
    file: File?,
    @StringRes missingMessageRes: Int,
    anchorView: View = window.decorView,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {

    watchVideo(file, getString(missingMessageRes), anchorView, forceChooser, chooserTitle)
}

fun Activity.watchVideoFromProvider(
    file: File?,
    @StringRes missingMessageRes: Int,
    anchorView: View = window.decorView,
    forceChooser: Boolean = false,
    chooserTitle: String = "",
    providerName: String = "$packageName.provider"
) {

    watchVideoFromProvider(file, getString(missingMessageRes), anchorView, forceChooser, chooserTitle, providerName)
}

fun Activity.watchVideo(
    file: File?,
    missingMessage: String?,
    anchorView: View = window.decorView,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {

    startActivityIntent(
        file?.let { getWatchVideoIntent(it) },
        missingMessage,
        anchorView,
        forceChooser,
        chooserTitle
    )
}

fun Activity.watchVideoFromProvider(
    file: File?,
    missingMessage: String?,
    anchorView: View = window.decorView,
    forceChooser: Boolean = false,
    chooserTitle: String = "",
    providerName: String = "$packageName.provider"
) {

    startActivityIntent(
        file?.let { getWatchVideoIntentWithProvider(it, providerName) },
        missingMessage,
        anchorView,
        forceChooser,
        chooserTitle
    )
}

//SupportFragment:

//Url:

fun SupportFragment.watchVideo(@StringRes urlRes: Int, forceChooser: Boolean = false, chooserTitle: String = "") {
    watchVideo(getString(urlRes), forceChooser, chooserTitle)
}

fun SupportFragment.watchVideo(url: String, forceChooser: Boolean = false, chooserTitle: String = "") {
    startActivityIntent(getWatchVideoIntent(url), forceChooser, chooserTitle)
}

fun SupportFragment.watchVideo(
    url: String?,
    @StringRes missingMessageRes: Int,
    anchorView: View? = view ?: activity?.window?.decorView,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {

    watchVideo(url, getString(missingMessageRes), anchorView, forceChooser, chooserTitle)
}

fun SupportFragment.watchVideo(
    url: String?,
    missingMessage: String?,
    anchorView: View? = view ?: activity?.window?.decorView,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {

    startActivityIntent(url?.let { getWatchVideoIntent(it) }, missingMessage, anchorView, forceChooser, chooserTitle)
}

//File:

fun SupportFragment.watchVideo(file: File, forceChooser: Boolean = false, chooserTitle: String = "") {
    startActivityIntent(getWatchVideoIntent(file), forceChooser, chooserTitle)
}

fun SupportFragment.watchVideoFromProvider(
    file: File,
    forceChooser: Boolean = false,
    chooserTitle: String = "",
    providerName: String = "${activity?.packageName}.provider"
) {

    startActivityIntent(getWatchVideoIntentWithProvider(file, providerName), forceChooser, chooserTitle)
}

fun SupportFragment.watchVideo(
    file: File?,
    @StringRes missingMessageRes: Int,
    anchorView: View? = view ?: activity?.window?.decorView,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {

    watchVideo(file, getString(missingMessageRes), anchorView, forceChooser, chooserTitle)
}

fun SupportFragment.watchVideoFromProvider(
    file: File?,
    @StringRes missingMessageRes: Int,
    anchorView: View? = view ?: activity?.window?.decorView,
    forceChooser: Boolean = false,
    chooserTitle: String = "",
    providerName: String = "${activity?.packageName}.provider"
) {

    watchVideoFromProvider(file, getString(missingMessageRes), anchorView, forceChooser, chooserTitle, providerName)
}

fun SupportFragment.watchVideo(
    file: File?,
    missingMessage: String?,
    anchorView: View? = view ?: activity?.window?.decorView,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {

    startActivityIntent(
        file?.let { getWatchVideoIntent(it) },
        missingMessage,
        anchorView,
        forceChooser,
        chooserTitle
    )
}

fun SupportFragment.watchVideoFromProvider(
    file: File?,
    missingMessage: String?,
    anchorView: View? = view ?: activity?.window?.decorView,
    forceChooser: Boolean = false,
    chooserTitle: String = "",
    providerName: String = "${activity?.packageName}.provider"
) {

    startActivityIntent(
        file?.let { getWatchVideoIntentWithProvider(it, providerName) },
        missingMessage,
        anchorView,
        forceChooser,
        chooserTitle
    )
}

//Context:

//Url:

fun Context.watchVideo(@StringRes urlRes: Int, forceChooser: Boolean = false, chooserTitle: String = "") {
    watchVideo(getString(urlRes), forceChooser, chooserTitle)
}

fun Context.watchVideo(url: String, forceChooser: Boolean = false, chooserTitle: String = "") {
    startActivityIntent(getWatchVideoIntent(url), forceChooser, chooserTitle)
}

fun Context.watchVideo(
    url: String?,
    @StringRes missingMessageRes: Int,
    anchorView: View? = null,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {

    watchVideo(url, getString(missingMessageRes), anchorView, forceChooser, chooserTitle)
}

fun Context.watchVideo(
    url: String?,
    missingMessage: String?,
    anchorView: View? = null,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {

    startActivityIntent(url?.let { getWatchVideoIntent(it) }, missingMessage, anchorView, forceChooser, chooserTitle)
}

//File:

fun Context.watchVideo(file: File, forceChooser: Boolean = false, chooserTitle: String = "") {
    startActivityIntent(getWatchVideoIntent(file), forceChooser, chooserTitle)
}

fun Context.watchVideoFromProvider(
    file: File,
    forceChooser: Boolean = false,
    chooserTitle: String = "",
    providerName: String = "$packageName.provider"
) {

    startActivityIntent(getWatchVideoIntentWithProvider(file, providerName), forceChooser, chooserTitle)
}

fun Context.watchVideo(
    file: File?,
    @StringRes missingMessageRes: Int,
    anchorView: View? = null,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {

    watchVideo(file, getString(missingMessageRes), anchorView, forceChooser, chooserTitle)
}

fun Context.watchVideoFromProvider(
    file: File?,
    @StringRes missingMessageRes: Int,
    anchorView: View? = null,
    forceChooser: Boolean = false,
    chooserTitle: String = "",
    providerName: String = "$packageName.provider"
) {

    watchVideoFromProvider(file, getString(missingMessageRes), anchorView, forceChooser, chooserTitle, providerName)
}

fun Context.watchVideo(
    file: File?,
    missingMessage: String?,
    anchorView: View? = null,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {

    startActivityIntent(
        file?.let { getWatchVideoIntent(it) },
        missingMessage,
        anchorView,
        forceChooser,
        chooserTitle
    )
}

fun Context.watchVideoFromProvider(
    file: File?,
    missingMessage: String?,
    anchorView: View? = null,
    forceChooser: Boolean = false,
    chooserTitle: String = "",
    providerName: String = "$packageName.provider"
) {

    startActivityIntent(
        file?.let { getWatchVideoIntentWithProvider(it, providerName) },
        missingMessage,
        anchorView,
        forceChooser,
        chooserTitle
    )
}
