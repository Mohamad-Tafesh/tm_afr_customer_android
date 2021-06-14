package com.tedmob.africell.features.services.domain


import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi

import com.tedmob.africell.data.api.dto.StatusDTO
import com.tedmob.africell.data.repository.domain.SessionRepository
import io.reactivex.Observable
import javax.inject.Inject

class ServiceUnSubscribeUseCase
@Inject constructor(
        private val restApi: RestApi,
        private val sessionRepository: SessionRepository,
        schedulers: ExecutionSchedulers)
    : UseCase<StatusDTO, String>(schedulers) {

    override fun buildUseCaseObservable(params: String): Observable<StatusDTO> {

        val subMsisdn = if (sessionRepository.selectedMsisdn != sessionRepository.msisdn) sessionRepository.selectedMsisdn else null
        return restApi.serviceUnSubscribe(subMsisdn,params)
    }
}