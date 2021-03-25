package com.tedmob.africell.features.settings.domain


import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi
import com.tedmob.africell.data.repository.domain.SessionRepository

import io.reactivex.Observable
import javax.inject.Inject

class LogoutUseCase
@Inject constructor(
    private val restApi: RestApi,
    private val sessionRepository: SessionRepository,
    schedulers: ExecutionSchedulers
) : UseCase<Unit, Unit>(schedulers) {

    override fun buildUseCaseObservable(params: Unit): Observable<Unit> {
        return restApi.logout()
    }


}
