package com.tedmob.africell.features.myBundlesAndServices.domain


import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi

import com.tedmob.africell.data.api.dto.MyBundlesAndServices
import com.tedmob.africell.data.repository.domain.SessionRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetMyBundlesServicesUseCase
@Inject constructor(
        private val restApi: RestApi,
        private val sessionRepository: SessionRepository,
        schedulers: ExecutionSchedulers)
    : UseCase<List<MyBundlesAndServices>, Unit>(schedulers) {

    override fun buildUseCaseObservable(params: Unit): Observable<List<MyBundlesAndServices>> {

        val subMsisdn = if (sessionRepository.selectedMsisdn != sessionRepository.msisdn) sessionRepository.selectedMsisdn else null
        return restApi.getMyBundlesAndServices(subMsisdn)
    }
}