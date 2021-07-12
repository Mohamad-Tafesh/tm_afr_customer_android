package com.africell.africell.features.authentication.domain

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi
import com.africell.africell.data.api.requests.RefreshTokenRequest
import com.africell.africell.data.repository.domain.SessionRepository
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Login user using name, email and phone number
 *
 * Save session when successful
 */
class RefreshTokenUseCase
@Inject constructor(
    private val api: RestApi,
    private val session: SessionRepository,
    private val firebaseAnalytics: FirebaseAnalytics,
    private val firebaseCrashlytics: FirebaseCrashlytics,
    schedulers: ExecutionSchedulers
) : UseCase<Unit, Unit>(schedulers) {

    //Version not waiting for OneSignal:
    override fun buildUseCaseObservable(params: Unit): Observable<Unit> {
        return api.refreshToken(RefreshTokenRequest(session.accessToken.orEmpty(),session.refreshToken.orEmpty()))
            .map { user ->
                // save session
               session.accessToken = user.authenticationToken.orEmpty()
                session.refreshToken= user.refreshToken.orEmpty()

                //user
            }
    }

}
