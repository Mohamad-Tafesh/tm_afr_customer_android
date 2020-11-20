@file:JvmName("EmailIntentUtils")

package com.tedmob.africell.util.intents

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment as SupportFragment

//Intents:

fun getEmailIntent(
    to: String? = null,
    cc: String? = null,
    subject: String = "",
    message: String = ""
) =

    getEmailIntent(
        to?.let { arrayOf(it) } ?: arrayOf(""),
        cc?.let { arrayOf(it) } ?: emptyArray(),
        subject,
        message
    )

fun getEmailIntent(
    toArray: Array<String>,
    ccArray: Array<String> = arrayOf(),
    subject: String = "",
    message: String = ""
) =
    Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse(
            toArray.takeIf { it.isNotEmpty() }
                ?.let { "mailto:${toArray.joinToString(",")}" }
                .orEmpty() +

                    mapOf<String?, String?>(
                        "cc" to ccArray.takeIf { it.isNotEmpty() }?.joinToString(","),
                        "subject" to Uri.encode(subject),
                        "body" to Uri.encode(message.replace("\n", "<br/>"))
                    ).combineIntoGETQueries()
        )
    }

fun Context.getEmailIntent(
    @StringRes to: Int? = 0,
    @StringRes cc: Int? = 0,
    @StringRes subjectRes: Int = 0,
    @StringRes messageRes: Int = 0
) =
    resources.getEmailIntent(to, cc, subjectRes, messageRes)

fun Context.getEmailIntent(
    @StringRes toResArray: Array<Int>,
    @StringRes ccResArray: Array<Int> = arrayOf(),
    @StringRes subjectRes: Int = 0,
    @StringRes messageRes: Int = 0
) =
    resources.getEmailIntent(toResArray, ccResArray, subjectRes, messageRes)

fun SupportFragment.getEmailIntent(
    @StringRes to: Int? = 0,
    @StringRes cc: Int? = 0,
    @StringRes subjectRes: Int = 0,
    @StringRes messageRes: Int = 0
) =
    resources.getEmailIntent(to, cc, subjectRes, messageRes)

fun SupportFragment.getEmailIntent(
    @StringRes toResArray: Array<Int>,
    @StringRes ccResArray: Array<Int> = arrayOf(),
    @StringRes subjectRes: Int = 0,
    @StringRes messageRes: Int = 0
) =
    resources.getEmailIntent(toResArray, ccResArray, subjectRes, messageRes)

fun Resources.getEmailIntent(
    @StringRes to: Int? = 0,
    @StringRes cc: Int? = 0,
    @StringRes subjectRes: Int = 0,
    @StringRes messageRes: Int = 0
) =
    getEmailIntent(
        to?.let { arrayOf(getString(it)) } ?: arrayOf(""),
        cc?.let { arrayOf(getString(it)) } ?: emptyArray(),
        subjectRes.takeUnless { it == 0 }?.let { getString(it) } ?: "",
        messageRes.takeUnless { it == 0 }?.let { getString(it) } ?: ""
    )

fun Resources.getEmailIntent(
    @StringRes toResArray: Array<Int>,
    @StringRes ccResArray: Array<Int> = arrayOf(),
    @StringRes subjectRes: Int = 0,
    @StringRes messageRes: Int = 0
) =
    getEmailIntent(
        toResArray.map { getString(it) }.toTypedArray(),
        ccResArray.map { getString(it) }.toTypedArray(),
        subjectRes.takeUnless { it == 0 }?.let { getString(it) } ?: "",
        messageRes.takeUnless { it == 0 }?.let { getString(it) } ?: ""
    )

//Activity:

fun Activity.email(
    @StringRes to: Int? = null,
    @StringRes cc: Int? = null,
    @StringRes subjectRes: Int = 0,
    @StringRes messageRes: Int = 0,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {

    startActivityIntent(getEmailIntent(to, cc, subjectRes, messageRes), forceChooser, chooserTitle)
}

fun Activity.email(
    to: String? = null,
    cc: String? = null,
    subject: String = "",
    message: String = "",
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {

    startActivityIntent(getEmailIntent(to, cc, subject, message), forceChooser, chooserTitle)
}

fun Activity.email(
    @StringRes toResArray: Array<Int>,
    @StringRes ccResArray: Array<Int> = arrayOf(),
    @StringRes subjectRes: Int = 0,
    @StringRes messageRes: Int = 0,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {

    startActivityIntent(
        getEmailIntent(toResArray, ccResArray, subjectRes, messageRes),
        forceChooser,
        chooserTitle
    )
}

fun Activity.email(
    toArray: Array<String>,
    ccArray: Array<String> = arrayOf(),
    subject: String,
    message: String,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {

    startActivityIntent(
        getEmailIntent(toArray, ccArray, subject, message),
        forceChooser,
        chooserTitle
    )
}

//Support fragment:

fun SupportFragment.email(
    @StringRes to: Int? = null,
    @StringRes cc: Int? = null,
    @StringRes subjectRes: Int = 0,
    @StringRes messageRes: Int = 0,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {

    startActivityIntent(getEmailIntent(to, cc, subjectRes, messageRes), forceChooser, chooserTitle)
}

fun SupportFragment.email(
    to: String? = null,
    cc: String? = null,
    subject: String = "",
    message: String = "",
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {

    startActivityIntent(getEmailIntent(to, cc, subject, message), forceChooser, chooserTitle)
}

fun SupportFragment.email(
    @StringRes toResArray: Array<Int>,
    @StringRes ccResArray: Array<Int> = arrayOf(),
    @StringRes subjectRes: Int = 0,
    @StringRes messageRes: Int = 0,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {

    startActivityIntent(
        getEmailIntent(toResArray, ccResArray, subjectRes, messageRes),
        forceChooser,
        chooserTitle
    )
}

fun SupportFragment.email(
    toArray: Array<String>,
    ccArray: Array<String> = arrayOf(),
    subject: String,
    message: String,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {

    startActivityIntent(
        getEmailIntent(toArray, ccArray, subject, message),
        forceChooser,
        chooserTitle
    )
}

//Context:

fun Context.email(
    @StringRes to: Int? = null,
    @StringRes cc: Int? = null,
    @StringRes subjectRes: Int = 0,
    @StringRes messageRes: Int = 0,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {

    startActivityIntent(getEmailIntent(to, cc, subjectRes, messageRes), forceChooser, chooserTitle)
}

fun Context.email(
    to: String? = null,
    cc: String? = null,
    subject: String = "",
    message: String = "",
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {

    startActivityIntent(getEmailIntent(to, cc, subject, message), forceChooser, chooserTitle)
}

fun Context.email(
    @StringRes toResArray: Array<Int>,
    @StringRes ccResArray: Array<Int> = arrayOf(),
    @StringRes subjectRes: Int = 0,
    @StringRes messageRes: Int = 0,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {

    startActivityIntent(
        getEmailIntent(toResArray, ccResArray, subjectRes, messageRes),
        forceChooser,
        chooserTitle
    )
}

fun Context.email(
    toArray: Array<String>,
    ccArray: Array<String> = arrayOf(),
    subject: String,
    message: String,
    forceChooser: Boolean = false,
    chooserTitle: String = ""
) {

    startActivityIntent(
        getEmailIntent(toArray, ccArray, subject, message),
        forceChooser,
        chooserTitle
    )
}


//---

private fun Map<String?, String?>.combineIntoGETQueries(
    addQueryIfValueNull: Boolean = false,
    addQuestionMark: Boolean = true
): String {
    return mapNotNull {
        val (key, value) = it
        if (key != null) {
            when {
                addQueryIfValueNull -> "$key=${value ?: ""}"
                value != null -> "$key=$value"
                else -> null
            }
        } else {
            null
        }
    }.combineIntoGETQueries(addQuestionMark)
}

private fun List<String>.combineIntoGETQueries(addQuestionMark: Boolean = true): String {
    return mapIndexed { index, s ->
        val firstPrefix = if (addQuestionMark) "?" else ""
        "${if (index == 0) firstPrefix else "&"}$s"
    }.joinToString("")
}