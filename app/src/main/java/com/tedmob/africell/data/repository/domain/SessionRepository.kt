package com.tedmob.africell.data.repository.domain

import com.tedmob.africell.data.api.dto.UserDTO

interface SessionRepository {
    var accessToken: String
    var refreshToken: String
    var msisdn:String

    var user: UserDTO?

    var language: String

    var userChangedLanguage: Boolean

    fun invalidateSession()

    fun isLoggedIn(): Boolean
}
