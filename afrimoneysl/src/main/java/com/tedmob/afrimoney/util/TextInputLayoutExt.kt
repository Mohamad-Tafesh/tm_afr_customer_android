@file:JvmName("TextInputLayoutUtils")

package com.tedmob.afrimoney.util

import androidx.annotation.StringRes
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.getText(): String {
    return editText?.text?.toString() ?: ""
}

fun TextInputLayout.setText(text: CharSequence) {
    editText?.setText(text)
}

fun TextInputLayout.setText(@StringRes resId: Int) {
    editText?.setText(resId)
}

fun TextInputLayout.setTextWhenNotBlank(text: CharSequence) {
    if (text.isNotBlank())
        editText?.setText(text)
}
