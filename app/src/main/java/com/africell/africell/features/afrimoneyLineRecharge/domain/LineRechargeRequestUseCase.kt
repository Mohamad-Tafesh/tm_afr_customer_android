package com.africell.africell.features.afrimoneyLineRecharge.domain


import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi

import com.africell.africell.data.api.dto.StatusDTO
import com.africell.africell.data.api.requests.ActivateBundleRequest
import com.africell.africell.data.api.requests.afrimoney.AirlineRequest
import com.africell.africell.data.api.requests.afrimoney.P2PRequest
import com.africell.africell.data.repository.domain.SessionRepository
import io.reactivex.Observable
import javax.inject.Inject

class LineRechargeRequestUseCase
@Inject constructor(
        private val restApi: RestApi,
        private val sessionRepository: SessionRepository,
        schedulers: ExecutionSchedulers)
    : UseCase<StatusDTO, AirlineRequest>(schedulers) {

    override fun buildUseCaseObservable(params: AirlineRequest): Observable<StatusDTO> {
         return restApi.purchaseAirline(params)
    }
}