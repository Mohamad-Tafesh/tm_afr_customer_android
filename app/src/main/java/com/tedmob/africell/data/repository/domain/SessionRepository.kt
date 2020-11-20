package com.tedmob.africell.data.repository.domain

import com.tedmob.africell.data.api.dto.UserDTO
import com.tedmob.africell.data.entity.User

interface SessionRepository {
    var accessToken: String

    var user: UserDTO?

    var language: String

    var userChangedLanguage: Boolean

    fun invalidateSession()

    fun isLoggedIn(): Boolean
}
