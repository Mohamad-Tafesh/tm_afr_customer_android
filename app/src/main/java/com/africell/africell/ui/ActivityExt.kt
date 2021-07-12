@file:JvmName("ActivityUtils")

package com.africell.africell.ui

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard() {
    val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    val view = currentFocus ?: View(this)

    manager?.hideSoftInputFromWindow(view.windowToken, 0)
}