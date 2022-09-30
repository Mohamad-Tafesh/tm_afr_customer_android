package com.africell.africell.features.settings.domain


import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi
import com.africell.africell.data.repository.domain.SessionRepository

import io.reactivex.Observable
import javax.inject.Inject

class DeleteAccountUseCase
@Inject constructor(
    private val restApi: RestApi,
    private val sessionRepository: SessionRepository,
    schedulers: ExecutionSchedulers
) : UseCase<Unit, Unit>(schedulers) {

    override fun buildUseCaseObservable(params: Unit): Observable<Unit> {
        return restApi.deleteAccount()
    }


}
