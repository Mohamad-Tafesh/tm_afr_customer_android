package com.tedmob.afrimoney.features.authentication.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.analytics.AnalyticsHandler
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.api.dto.GenerateOtpDTO
import com.tedmob.afrimoney.data.crashlytics.CrashlyticsHandler
import com.tedmob.afrimoney.data.entity.User
import com.tedmob.afrimoney.data.entity.UserState
import com.tedmob.afrimoney.data.repository.domain.SessionRepository
import com.tedmob.afrimoney.exception.AppException
import com.tedmob.afrimoney.util.identifyUser
import com.tedmob.afrimoney.util.security.StringEncryptor
import com.tedmob.afrimoney.util.security.encryptWith
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named


class RegisterUseCase
@Inject constructor(
    private val api: TedmobApis,
    private val session: SessionRepository,
    private val analytics: AnalyticsHandler,
    private val crashlytics: CrashlyticsHandler,
    @Named("local-string") private val encryptor: StringEncryptor,
) : SuspendableUseCase<UserState, RegisterUseCase.UserLoginInfo>() {

    override suspend fun execute(params: UserLoginInfo): UserState {
        return withContext(Dispatchers.IO) {
            lateinit var idtype: String
            when (params.idType) {
                "National ID Card" -> idtype = "NATIONAL_ID"
                "Driver’s license" -> idtype = "DRIVER_CARD"
                "Voter ID Card" -> idtype = "VOTER_CARD"
                "Passport" -> idtype = "PASSPORT"
            }


            val response = api.register(
                params.mobilenb,
                params.firstName,
                params.lastName,
                params.idNumber,
                idtype,
                params.dob,
                params.gender,
                params.street,
                params.city,
                params.district,
                params.idNumber,
                params.idType
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
                UserState.NotRegistered(session.msisdn)
            }

        }
    }

    data class UserLoginInfo(
        val mobilenb: String,
        val firstName: String,
        val lastName: String,
        val idNumber: String,
        val idType: String,
        val dob: String, //20081980
        val gender: String,
        val street: String,
        val city: String,
        val district: String,
    )
}
