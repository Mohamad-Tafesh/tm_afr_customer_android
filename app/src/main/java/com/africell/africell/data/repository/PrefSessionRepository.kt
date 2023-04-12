package com.africell.africell.data.repository

import android.app.Application
import com.google.gson.Gson
import com.africell.africell.data.api.dto.UserDTO
import com.africell.africell.data.repository.domain.SessionRepository
import com.africell.africell.util.PrefUtils
import com.africell.africell.util.preference.RxPrefObjectProperty
import com.africell.africell.util.preference.RxPrefProperty
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefSessionRepository
@Inject
constructor(prefUtils: PrefUtils, gson: Gson, val context: Application) : SessionRepository {

    override fun invalidateSession() {
        accessToken = ""
        refreshToken = ""
        verificationToken = ""
        user = null
        msisdn = ""
        selectedMsisdn = ""
    }

    override var accessToken: String by RxPrefProperty(prefUtils.accessToken)
    override var refreshToken: String by RxPrefProperty(prefUtils.refreshToken)
    override var msisdn: String by RxPrefProperty(prefUtils.msisdn)
    override var selectedMsisdn: String by RxPrefProperty(prefUtils.selectedMsisdn)

    override var user: UserDTO? by RxPrefObjectProperty(prefUtils.user, gson, UserDTO::class.java)

    override var language: String by RxPrefProperty(prefUtils.language)

    override var userChangedLanguage: Boolean by RxPrefProperty(prefUtils.userChangedLanguage)

    override fun isLoggedIn(): Boolean {
        return accessToken.isNotBlank()
    }

    override var showHelp: Boolean by RxPrefProperty(prefUtils.showHelp)

    override var verificationToken: String by RxPrefProperty(prefUtils.verificationToken)

    override var hasRefusedNotificationsPermission: Boolean by RxPrefProperty(prefUtils.hasRefusedNotificationsPermission)

    override var msisdnAfrimoney: String by RxPrefProperty(prefUtils.msisdnAfrimoney)


    override var deviceTokenAfrimoney: String by RxPrefProperty(prefUtils.deviceTokenAfrimoney)

    override var accessTokenAfrimoney: String by RxPrefProperty(prefUtils.accessTokenAfrimoney)


}