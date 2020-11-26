@file:JvmName("TextInputLayoutUtils")

package com.tedmob.africell.util

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.textfield.TextInputLayout
import com.tedmob.africell.ui.spinner.MaterialSpinner
import com.tedmob.africell.ui.spinner.OnItemSelectedListener

fun TextInputLayout.getText(): String {
    return editText?.text?.toString() ?: ""
}

fun TextInputLayout.setText(text: CharSequence) {
    editText?.setText(text)
}

fun TextInputLayout.setText(@StringRes resId: Int) {
    editText?.setText(resId)
}

fun TextInputLayout.setTextWhenNotBlank(text: CharSequence?) {
    if (!text.isNullOrBlank())
        editText?.setText(text)
}

fun TextInputLayout.saveText( action: ((item: String) -> Unit)) {
    editText?.addTextChangedListener(
        object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                action.invoke(p0.toString())
            }
        }
    )
}

fun MaterialSpinner.saveObject(items:List<Any>, action: ((item: Any?) -> Unit)) {
    onItemSelectedListener=
        object : OnItemSelectedListener {
            override fun onItemSelected(parent: MaterialSpinner, view: View?, position: Int, id: Long) {
                action.invoke(items[position])
            }

            override fun onNothingSelected(parent: MaterialSpinner) {
                action.invoke(null)
            }

        }
}