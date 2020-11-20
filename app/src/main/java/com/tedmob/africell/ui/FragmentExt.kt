@file:JvmName("FragmentUtils")

package com.tedmob.africell.ui

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Fragment.hideKeyboard() {
    val manager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    val focusedView = view ?: activity?.let { it.currentFocus ?: View(it) }

    focusedView?.let { manager?.hideSoftInputFromWindow(it.windowToken, 0) }
}