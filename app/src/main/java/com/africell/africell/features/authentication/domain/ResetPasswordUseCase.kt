package com.africell.africell.features.authentication.domain

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi
import com.africell.africell.data.api.requests.ForgotPasswordRequest
import com.africell.africell.data.repository.domain.SessionRepository
import com.africell.africell.util.identifyUser
import io.reactivex.Observable
import javax.inject.Inject

class ResetPasswordUseCase
@Inject constructor(
    private val restApi: RestApi,
    private val session: SessionRepository,
    private val firebaseAnalytics: FirebaseAnalytics,
    private val firebaseCrashlytics: FirebaseCrashlytics,
    schedulers: ExecutionSchedulers
) : UseCase<Unit, ResetPasswordUseCase.Params>(schedulers) {

    override fun buildUseCaseObservable(params: Params): Observable<Unit> {

        return restApi.resetPassword(
            ForgotPasswordRequest(
                session.verificationToken,
                params.password,
                params.confirmPassword
            )
        ).map { user ->
            // save session
            session.accessToken = user.authenticationToken.orEmpty()
            session.refreshToken = user.refreshToken.orEmpty()
            session.identifyUser(firebaseAnalytics, firebaseCrashlytics)
            //user
        }

    }

    data class Params(
        val password: String?,
        val confirmPassword: String?
    )
}