package com.tedmob.africell.features.authentication.domain

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi
import com.tedmob.africell.data.api.dto.UserDTO
import com.tedmob.africell.data.entity.User
import com.tedmob.africell.data.repository.domain.SessionRepository
import com.tedmob.africell.exception.AppException
import com.tedmob.africell.util.identifyUser
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Login user using name, email and phone number
 *
 * Save session when successful
 */
class LoginUseCase
@Inject constructor(
    private val api: RestApi,
    private val session: SessionRepository,
    private val firebaseAnalytics: FirebaseAnalytics,
    private val firebaseCrashlytics: FirebaseCrashlytics,
    schedulers: ExecutionSchedulers
) : UseCase<Unit, LoginUseCase.UserLoginInfo>(schedulers) {

    //Version waiting for OneSignal:
    /*override fun buildUseCaseObservable(params: UserLoginInfo): Observable<Unit> {
        return oneSignalIdsObservable
            .observeOn(schedulers.subscribeScheduler)
            .flatMap {
                api.login(params.username, params.password)
                    .map { user ->
                        // save session
                        session.accessToken = user.accessToken
                session.user = user
                        session.identifyUser(firebaseAnalytics)
                        //user
                    }
            }
    }*/

    //Version not waiting for OneSignal:
    /*override fun buildUseCaseObservable(params: UserLoginInfo): Observable<Unit> {
        return api.login(params.username, params.password)
            .map { user ->
                // save session
                session.accessToken = user.accessToken
                session.user = user
                session.identifyUser(firebaseAnalytics)
                //user
            }
    }*/

    override fun buildUseCaseObservable(params: UserLoginInfo): Observable<Unit> {
        return Observable.just(params)
            .switchMap {
                if (true) {
                    Observable.just(
                        UserDTO(null,null,null,null,null,

                            null,null,null,null,null,
                            null,null,"1234","1234"
                        )
                    )
                } else {
                    Observable.error<UserDTO>(
                        AppException(
                            AppException.Code.NETWORK,
                            "Wrong username / password",
                            null
                        )
                    )
                }
            }
            .subscribeOn(Schedulers.io())
            .delay(2, TimeUnit.SECONDS, Schedulers.io(), true)
            .map { user ->
                // save session
                session.accessToken = user.token.orEmpty()
                session.user = user
                session.identifyUser(firebaseAnalytics, firebaseCrashlytics)
            }
    }

    data class UserLoginInfo(val username: String, val password: String)
}
