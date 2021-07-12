package com.africell.africell.app

import androidx.annotation.StringRes

interface StringLoader {
    fun getString(@StringRes resId: Int): String

    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String
}