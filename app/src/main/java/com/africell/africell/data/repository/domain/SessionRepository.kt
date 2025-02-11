package com.africell.africell.data.repository.domain

import com.africell.africell.data.api.dto.UserDTO

interface SessionRepository {
    var accessToken: String
    var refreshToken: String
    var msisdn:String
    var selectedMsisdn:String
    var verificationToken:String

    var user: UserDTO?

    var language: String

    var userChangedLanguage: Boolean
    var showHelp: Boolean

    fun invalidateSession()

    fun isLoggedIn(): Boolean

    var deviceTokenAfrimoney: String
    var accessTokenAfrimoney: String
    var msisdnAfrimoney: String


    var hasRefusedNotificationsPermission: Boolean //TODO move to another class that relies on another preferences file
}
