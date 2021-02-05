package com.tedmob.africell.features.authentication.domain

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi
import com.tedmob.africell.data.api.requests.RegisterRequest
import com.tedmob.africell.data.repository.domain.SessionRepository
import com.tedmob.africell.util.identifyUser
import io.reactivex.Observable
import javax.inject.Inject

class RegisterUseCase
@Inject constructor(
    private val restApi: RestApi,
    private val session: SessionRepository,
    private val firebaseAnalytics: FirebaseAnalytics,
    private val firebaseCrashlytics: FirebaseCrashlytics,
    schedulers: ExecutionSchedulers
) : UseCase<Unit, RegisterUseCase.Params>(schedulers) {

    override fun buildUseCaseObservable(params: Params): Observable<Unit> {


        return restApi.register(
            RegisterRequest(
                session.verificationToken,
                params.firstName, params.lastName, params.email, params.dob, params.password,
                params.confirmPassword
            )
        ).map { user ->
            session.accessToken = user.authenticationToken.orEmpty()
            session.refreshToken = user.refreshToken.orEmpty()
            session.identifyUser(firebaseAnalytics, firebaseCrashlytics)
        }
    }

    data class Params(
        val firstName: String?,
        val lastName: String?,
        val email: String?,
        val dob: String?,
        val password: String?,
        val confirmPassword: String?
    )
}