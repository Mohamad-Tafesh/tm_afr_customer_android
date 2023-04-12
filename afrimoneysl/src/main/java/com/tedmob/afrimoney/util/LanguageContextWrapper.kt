package com.tedmob.afrimoney.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import java.util.*

class LanguageContextWrapper {

    companion object {
        @SuppressLint("NewApi")
        fun wrap(context: Context, newLocale: Locale): Context {
            var newContext = context

            val res = newContext.resources
            val configuration = Configuration(res.configuration)

            //fixme Only remove deprecated code if compiler forces us to.
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                    configuration.setLocale(newLocale)

                    val localeList = LocaleList(newLocale)
                    LocaleList.setDefault(localeList)
                    configuration.setLocales(localeList)
                    configuration.setLocale(newLocale)

                    res.updateConfiguration(configuration, res.displayMetrics)
                    newContext = newContext.createConfigurationContext(configuration)
                }

                else -> {
                    Locale.setDefault(newLocale)

                    configuration.setLocale(newLocale)
                    configuration.setLayoutDirection(newLocale)

                    res.updateConfiguration(configuration, res.displayMetrics)
                    newContext = newContext.createConfigurationContext(configuration)
                }
            }

            return newContext
        }
    }

}