package com.tedmob.africell.data.repository

import android.app.Application
import android.content.Intent
import android.widget.Toast
import com.google.gson.Gson
import com.tedmob.africell.data.api.dto.UserDTO
import com.tedmob.africell.data.entity.User
import com.tedmob.africell.data.repository.domain.SessionRepository
import com.tedmob.africell.features.authentication.AuthenticationActivity
import com.tedmob.africell.util.PrefUtils
import com.tedmob.africell.util.preference.RxPrefObjectProperty
import com.tedmob.africell.util.preference.RxPrefProperty
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefSessionRepository
@Inject
constructor(prefUtils: PrefUtils, gson: Gson,val context: Application) : SessionRepository {

    override fun invalidateSession() {
        accessToken = ""
        refreshToken=""
        user = null
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

    override fun redirectToLogin() {
        invalidateSession()
        context.startActivity(Intent(context, AuthenticationActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        Toast.makeText(context, "Session Expired", Toast.LENGTH_LONG).show()
    }

    override var verificationToken: String by RxPrefProperty(prefUtils.verificationToken)

}