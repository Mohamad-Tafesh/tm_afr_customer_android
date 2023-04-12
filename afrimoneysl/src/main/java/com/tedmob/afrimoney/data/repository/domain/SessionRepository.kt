package com.tedmob.afrimoney.data.repository.domain

import com.tedmob.afrimoney.data.entity.User

interface SessionRepository {
    var deviceToken: String

    var accessToken: String

    var refreshToken: String

    var user: User?

    var language: String

    var userChangedLanguage: Boolean

    var msisdn: String

    fun invalidateSession()

    fun isLoggedIn(): Boolean
}
