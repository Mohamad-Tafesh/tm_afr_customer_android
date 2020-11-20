package com.tedmob.africell.features.authentication.domain

import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi
import com.tedmob.africell.data.repository.domain.SessionRepository
import io.reactivex.Observable
import javax.inject.Inject

class SetUserPushUseCase
@Inject constructor(
    private val restApi: RestApi,
    private val sessionRepository: SessionRepository,
    schedulers: ExecutionSchedulers
) : UseCase<Unit, Unit>(schedulers) {

    override fun buildUseCaseObservable(params: Unit): Observable<Unit> {
        return /*restApi.setPush(sessionRepository.pushId)*/ Observable.just(Unit)

    }


}
