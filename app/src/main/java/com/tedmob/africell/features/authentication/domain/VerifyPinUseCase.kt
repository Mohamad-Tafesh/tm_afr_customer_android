package com.tedmob.africell.features.authentication.domain

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi
import com.tedmob.africell.data.api.dto.UserDTO
import com.tedmob.africell.data.repository.domain.SessionRepository
import com.tedmob.africell.util.identifyUser
import io.reactivex.Observable
import javax.inject.Inject

class VerifyPinUseCase
@Inject constructor(
    private val restApi: RestApi,
    private val session: SessionRepository,
    private val firebaseAnalytics: FirebaseAnalytics,
    private val firebaseCrashlytics: FirebaseCrashlytics,
    schedulers: ExecutionSchedulers
) : UseCase<UserDTO, VerifyPinUseCase.Params>(schedulers) {


    override fun buildUseCaseObservable(params: Params): Observable<UserDTO> {
        return /*restApi.verifyPin(session.registrationId, params.pin).map {
            session.accessToken = it.token.orEmpty()
            session.user = it
            session.identifyUser(firebaseAnalytics,firebaseCrashlytics)
            it
        }*/ Observable.just(
            UserDTO(null,null,null,null,null,

                null,null,null,null,null,
                null,null,null,"1234"
            )
        )
    }

    data class Params(
        val pin: String
    )
}
