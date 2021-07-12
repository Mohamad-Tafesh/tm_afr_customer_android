package com.africell.africell.app

import android.content.Context
import androidx.annotation.StringRes

// https://proandroiddev.com/seven-principles-of-great-unit-tests-adapted-for-android-342515f98ef2
class AndroidStringLoader(private val context: Context) : StringLoader {
    override fun getString(@StringRes resId: Int): String {
        return context.getString(resId)
    }

    override fun getString(resId: Int, vararg formatArgs: Any): String {
        return context.getString(resId, formatArgs)
    }
}