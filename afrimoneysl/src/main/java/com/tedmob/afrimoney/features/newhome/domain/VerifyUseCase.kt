package com.tedmob.afrimoney.features.newhome.domain

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

class VerifyUseCase
@Inject constructor(
    private val api: TedmobApis,
    private val session: SessionRepository,
    private val analytics: AnalyticsHandler,
    private val crashlytics: CrashlyticsHandler,
    @Named("local-string") private val encryptor: StringEncryptor,
) : SuspendableUseCase<UserState, VerifyUseCase.Params>() {

    override suspend fun execute(params: Params): UserState {
        return withContext(Dispatchers.IO) {

            try {
             //   val info = api.userInfoCheck(params.number, params.token)
                val info = api.userInfo(params.number)
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
                session.accessToken = ""
                session.refreshToken = ""
                UserState.NotRegistered(params.number)
            }
        }
    }

    class Params(
        val number: String,
        val token: String
    )

}