package com.tedmob.afrimoney.features.authentication.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.analytics.AnalyticsHandler
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.crashlytics.CrashlyticsHandler
import com.tedmob.afrimoney.data.entity.User
import com.tedmob.afrimoney.data.entity.UserState
import com.tedmob.afrimoney.data.repository.domain.SessionRepository
import com.tedmob.afrimoney.exception.AppException
import com.tedmob.afrimoney.util.identifyUser
import com.tedmob.afrimoney.util.security.StringEncryptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class VerifyLoginUseCase
@Inject constructor(
    private val api: TedmobApis,
    private val session: SessionRepository,
    private val analytics: AnalyticsHandler,
    private val crashlytics: CrashlyticsHandler,
    @Named("local-string") private val encryptor: StringEncryptor,
) : SuspendableUseCase<UserState, VerifyLoginUseCase.UserLoginInfo>() {

    override suspend fun execute(params: UserLoginInfo): UserState {
        return withContext(Dispatchers.IO) {
            val response = api.validateOtp(
                params.mobilenb,
                params.otp,
            )

            try {
                val info = api.userInfo(params.mobilenb)
                val user = User(
                    info.msisdn.orEmpty(),
                    info.name.orEmpty(),
                )

                // save session
                session.user = user
                session.identifyUser(analytics, crashlytics)

                session.msisdn = user.phoneNumber

                UserState.Registered(user)

            } catch (e: AppException) {
                session.msisdn = ""
                session.accessToken=""
                session.refreshToken=""
                UserState.NotRegistered(params.mobilenb)
            }
        }
    }

    data class UserLoginInfo(val otp: String, val mobilenb: String)
}