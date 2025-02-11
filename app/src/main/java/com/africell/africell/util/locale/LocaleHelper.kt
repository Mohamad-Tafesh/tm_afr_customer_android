package com.africell.africell.util.locale

import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager
import com.africell.africell.BuildConfig.FLAVOR
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.google.gson.Gson
import com.africell.africell.R
import com.africell.africell.data.repository.PrefSessionRepository
import com.africell.africell.data.repository.domain.SessionRepository
import com.africell.africell.util.LanguageContextWrapper
import com.africell.africell.util.PrefUtils
import java.util.*

class LocaleHelper
constructor(private var context: Context) {

    private val session = getSessionRepository(context)

    fun createConfigurationContext(): Context {
        resolveLanguage(context)
        context = setLocale(context, session.language)
        return context
    }

    private fun resolveLanguage(context: Context) {
        if (!session.userChangedLanguage) {
            val supportedLanguages = context.resources.getStringArray(R.array.supported_language_codes)
            val deviceLanguage = Locale.getDefault().language

            if (FLAVOR == "drc") {
                session.language = supportedLanguages[0]
            }else if(supportedLanguages.contains(deviceLanguage)){
                session.language = deviceLanguage
            }
        }
    }

    companion object {
        @JvmStatic
        fun setLocale(context: Context, language: String): Context {
            val locale = Locale(language)
            return LanguageContextWrapper.wrap(context, locale)
        }

        /*
           LocaleHelper is used in Application's attachBaseContext before
           Application is injected. Therefore SessionRepository can't be injected at this level
           and we have to create an instance manually
         */
        private fun getSessionRepository(context: Context): SessionRepository {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val rxPrefs = RxSharedPreferences.create(prefs)
            val preUtils = PrefUtils(rxPrefs)
            val gson = Gson()//not used here
            return PrefSessionRepository(preUtils, gson, Application())
        }
    }

}
