package com.tedmob.afrimoney.util

import com.f2prateek.rx.preferences2.RxSharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefUtils
@Inject
constructor(preferences: RxSharedPreferences) {

    val userChangedLanguage = preferences.getBoolean("user_changed_language", false)
    val deviceToken = preferences.getString("device_token", "")
    val accessToken = preferences.getString("access_token", "")

    val refreshToken = preferences.getString("refresh_token", "")
    val language = preferences.getString("pref_language", "en")

    val user = preferences.getString("pref_user", "")
    val pin = preferences.getString("pref_pin", "")
    val msisdn = preferences.getString("msisdn", "")
}
