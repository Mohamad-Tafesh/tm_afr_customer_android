@file:JvmName("FragmentUtils")

package com.tedmob.afrimoney.ui

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Fragment.hideKeyboard() {
    val manager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    val focusedView = view ?: activity?.let { it.currentFocus ?: View(it) }

    focusedView?.let { manager?.hideSoftInputFromWindow(it.windowToken, 0) }
}

inline val Fragment.statusBarHeight: Int
    get() {
        var result = 0
        val resourceId: Int = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }