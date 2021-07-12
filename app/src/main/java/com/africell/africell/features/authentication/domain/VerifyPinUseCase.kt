package com.africell.africell.features.authentication.domain

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi
import com.africell.africell.data.api.dto.VerifyOTPDTO
import com.africell.africell.data.api.requests.VerifyOTPRequest
import com.africell.africell.data.repository.domain.SessionRepository
import io.reactivex.Observable
import javax.inject.Inject

class VerifyPinUseCase
@Inject constructor(
    private val restApi: RestApi,
    private val session: SessionRepository,
    private val firebaseAnalytics: FirebaseAnalytics,
    private val firebaseCrashlytics: FirebaseCrashlytics,
    schedulers: ExecutionSchedulers
) : UseCase<VerifyOTPDTO, VerifyPinUseCase.Params>(schedulers) {


    override fun buildUseCaseObservable(params: Params): Observable<VerifyOTPDTO> {
        return restApi.verifyOTP(VerifyOTPRequest( session.msisdn, params.pin)).map {
            session.verificationToken = it.verificationToken.orEmpty()
            it
        }
    }

    data class Params(
        val pin: String
    )
}
