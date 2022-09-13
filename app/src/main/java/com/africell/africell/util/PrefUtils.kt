package com.africell.africell.util

import com.f2prateek.rx.preferences2.RxSharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefUtils
@Inject
constructor(preferences: RxSharedPreferences) {

    val userChangedLanguage = preferences.getBoolean("user_changed_language", false)
    val accessToken = preferences.getString("access_token", "")
    val refreshToken = preferences.getString("refresh_token", "")
    val msisdn = preferences.getString("msisdn", "")
    val selectedMsisdn = preferences.getString("selected_msisdn", "")
    val verificationToken = preferences.getString("verificationToken", "")
    val language = preferences.getString("pref_language", "fr")

    val user = preferences.getString("pref_user", "")
    val showHelp=preferences.getBoolean("show_help", true)
}
