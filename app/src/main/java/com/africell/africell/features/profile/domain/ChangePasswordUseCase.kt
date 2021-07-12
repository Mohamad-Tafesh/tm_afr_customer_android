package com.africell.africell.features.profile.domain

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi
import com.africell.africell.data.api.dto.ChangePasswordDTO
import com.africell.africell.data.api.requests.ChangePasswordRequest
import com.africell.africell.data.repository.domain.SessionRepository
import io.reactivex.Observable
import javax.inject.Inject

class ChangePasswordUseCase
@Inject constructor(
    private val restApi: RestApi,
    private val session: SessionRepository,
    private val firebaseAnalytics: FirebaseAnalytics,
    private val firebaseCrashlytics: FirebaseCrashlytics,
    schedulers: ExecutionSchedulers
) : UseCase<ChangePasswordDTO, ChangePasswordUseCase.Params>(schedulers) {

    override fun buildUseCaseObservable(params: Params): Observable<ChangePasswordDTO> {

        return restApi.changePassword(
            ChangePasswordRequest(
                params.oldPassword,
                params.password,
                params.confirmPassword
            )
        ).map {
            session.accessToken = it.authenticationToken.orEmpty()
            session.refreshToken = it.refreshToken.orEmpty()
            it
        }

    }

    data class Params(
        val oldPassword: String?,
        val password: String?,
        val confirmPassword: String?
    )
}