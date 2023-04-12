package com.tedmob.afrimoney.features.authentication.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.analytics.AnalyticsHandler
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.crashlytics.CrashlyticsHandler
import com.tedmob.afrimoney.data.entity.User
import com.tedmob.afrimoney.data.repository.domain.SessionRepository
import com.tedmob.afrimoney.exception.AppException
import com.tedmob.afrimoney.util.identifyUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VerifyPinUseCase
@Inject constructor(
    private val api: TedmobApis,
    private val session: SessionRepository,
    private val analytics: AnalyticsHandler,
    private val crashlytics: CrashlyticsHandler,
) : SuspendableUseCase<Unit, VerifyPinUseCase.UserLoginInfo>() {

    override suspend fun execute(params: UserLoginInfo) {
        return withContext(Dispatchers.IO) {
            val response = api.login(
                params.mobilenb,
                params.pin,
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
                session.accessToken = response.token
                session.refreshToken = response.refreshToken


            } catch (e: AppException) {
                session.msisdn = ""
                session.refreshToken = ""
                session.accessToken = ""

            }
        }
    }


    data class UserLoginInfo(val pin: String, val mobilenb: String)
}