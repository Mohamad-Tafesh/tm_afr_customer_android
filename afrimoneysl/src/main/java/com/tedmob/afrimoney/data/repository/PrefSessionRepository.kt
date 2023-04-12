package com.tedmob.afrimoney.data.repository

import com.google.gson.Gson
import com.tedmob.afrimoney.data.entity.User
import com.tedmob.afrimoney.data.repository.domain.SessionRepository
import com.tedmob.afrimoney.util.PrefUtils
import com.tedmob.afrimoney.util.preference.RxPrefEncryptedObjectProperty
import com.tedmob.afrimoney.util.preference.RxPrefEncryptedProperty
import com.tedmob.afrimoney.util.preference.RxPrefProperty
import com.tedmob.afrimoney.util.security.StringEncryptor
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class PrefSessionRepository
@Inject
constructor(prefUtils: PrefUtils, gson: Gson, @Named("local-string") encryptor: StringEncryptor) : SessionRepository {

    override fun invalidateSession() {
        accessToken = ""
        refreshToken = ""

        user = null
        msisdn = ""
    }

    override var deviceToken: String by RxPrefEncryptedProperty(prefUtils.deviceToken, encryptor)

    override var accessToken: String by RxPrefEncryptedProperty(prefUtils.accessToken, encryptor)


    override var refreshToken: String by RxPrefEncryptedProperty(prefUtils.refreshToken, encryptor)

    override var user: User? by RxPrefEncryptedObjectProperty(prefUtils.user, gson, User::class.java, encryptor)

    override var language: String by RxPrefProperty(prefUtils.language)

    override var userChangedLanguage: Boolean by RxPrefProperty(prefUtils.userChangedLanguage)

    override var msisdn: String by RxPrefEncryptedProperty(prefUtils.msisdn, encryptor)

    override fun isLoggedIn(): Boolean {
        return msisdn.isNotBlank() && accessToken.isNotBlank()
    }
}