@file:JvmName("ViewUtils")

package com.tedmob.africell.ui

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

inline fun View.hideKeyboard() {
    val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    manager?.hideSoftInputFromWindow(windowToken, 0)
}