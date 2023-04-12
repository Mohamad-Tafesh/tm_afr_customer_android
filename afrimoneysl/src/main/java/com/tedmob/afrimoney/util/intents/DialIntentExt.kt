@file:JvmName("DialIntentUtils")

package com.tedmob.afrimoney.util.intents

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment as SupportFragment

//Intents:

fun getDialIntent() = Intent(Intent.ACTION_DIAL)

fun getDialIntent(phone: String) = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))

fun Context.getDialIntent(@StringRes phoneRes: Int) = resources.getDialIntent(phoneRes)

fun SupportFragment.getDialIntent(@StringRes phoneRes: Int) = resources.getDialIntent(phoneRes)

fun Resources.getDialIntent(@StringRes phoneRes: Int) = getDialIntent(getString(phoneRes))

//Activity:

fun Activity.dial(@StringRes phoneRes: Int, forceChooser: Boolean = false, chooserTitle: String = "") {
    startActivityIntent(getDialIntent(phoneRes), forceChooser, chooserTitle)
}

fun Activity.dial(phone: String, forceChooser: Boolean = false, chooserTitle: String = "") {
    startActivityIntent(getDialIntent(phone), forceChooser, chooserTitle)
}

fun Activity.openDialer(forceChooser: Boolean = false, chooserTitle: String = "") {
    startActivityIntent(getDialIntent(), forceChooser, chooserTitle)
}

//Support fragment:

fun SupportFragment.dial(@StringRes phoneRes: Int, forceChooser: Boolean = false, chooserTitle: String = "") {
    startActivityIntent(getDialIntent(phoneRes), forceChooser, chooserTitle)
}

fun SupportFragment.dial(phone: String, forceChooser: Boolean = false, chooserTitle: String = "") {
    startActivityIntent(getDialIntent(phone), forceChooser, chooserTitle)
}

fun SupportFragment.openDialer(forceChooser: Boolean = false, chooserTitle: String = "") {
    startActivityIntent(getDialIntent(), forceChooser, chooserTitle)
}

//Context:

fun Context.dial(@StringRes phoneRes: Int, forceChooser: Boolean = false, chooserTitle: String = "") {
    startActivityIntent(getDialIntent(phoneRes), forceChooser, chooserTitle)
}

fun Context.dial(phone: String, forceChooser: Boolean = false, chooserTitle: String = "") {
    startActivityIntent(getDialIntent(phone), forceChooser, chooserTitle)
}

fun Context.openDialer(forceChooser: Boolean = false, chooserTitle: String = "") {
    startActivityIntent(getDialIntent(), forceChooser, chooserTitle)
}