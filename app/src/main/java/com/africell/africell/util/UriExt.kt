package com.africell.africell.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.URLUtil
import androidx.fragment.app.Fragment

@JvmName("UriUtils")

fun Uri.extension(): String {
    return path?.let {
        val index = it.lastIndexOf(".")
        if (index >= 0) {
            it.substring(index)
        } else {
            null
        }
    }.orEmpty()
}

fun Fragment.actionView(url: String?) {
    context?.actionView(url)
}

fun Context.actionView(url: String?) {
    if (url != null && URLUtil.isValidUrl(url)) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}
