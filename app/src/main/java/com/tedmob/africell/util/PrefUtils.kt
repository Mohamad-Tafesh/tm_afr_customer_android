package com.tedmob.africell.util

import com.f2prateek.rx.preferences2.RxSharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefUtils
@Inject
constructor(preferences: RxSharedPreferences) {

    val userChangedLanguage = preferences.getBoolean("user_changed_language", false)
    val accessToken = preferences.getString("access_token", "")
    val language = preferences.getString("pref_language", "en")

    val user = preferences.getString("pref_user", "")
}
