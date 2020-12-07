package com.tedmob.africell.data.repository

import com.google.gson.Gson
import com.tedmob.africell.data.api.dto.UserDTO
import com.tedmob.africell.data.entity.User
import com.tedmob.africell.data.repository.domain.SessionRepository
import com.tedmob.africell.util.PrefUtils
import com.tedmob.africell.util.preference.RxPrefObjectProperty
import com.tedmob.africell.util.preference.RxPrefProperty
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefSessionRepository
@Inject
constructor(prefUtils: PrefUtils, gson: Gson) : SessionRepository {

    override fun invalidateSession() {
        accessToken = ""
        refreshToken=""
        user = null
    }

    override var accessToken: String by RxPrefProperty(prefUtils.accessToken)
    override var refreshToken: String by RxPrefProperty(prefUtils.refreshToken)
    override var msisdn: String by RxPrefProperty(prefUtils.msisdn)

    override var user: UserDTO? by RxPrefObjectProperty(prefUtils.user, gson, UserDTO::class.java)

    override var language: String by RxPrefProperty(prefUtils.language)

    override var userChangedLanguage: Boolean by RxPrefProperty(prefUtils.userChangedLanguage)

    override fun isLoggedIn(): Boolean {
        return accessToken.isNotBlank()
    }
}