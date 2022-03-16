package com.africell.africell.features.afrimoneyMerchantPaymnet.domain


import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi

import com.africell.africell.data.api.dto.StatusDTO
import com.africell.africell.data.api.requests.ActivateBundleRequest
import com.africell.africell.data.api.requests.afrimoney.MerchantPayRequest
import com.africell.africell.data.api.requests.afrimoney.P2PRequest
import com.africell.africell.data.repository.domain.SessionRepository
import io.reactivex.Observable
import javax.inject.Inject

class AfrimoneyMerchantPayUseCase
@Inject constructor(
        private val restApi: RestApi,
        private val sessionRepository: SessionRepository,
        schedulers: ExecutionSchedulers)
    : UseCase<StatusDTO, MerchantPayRequest>(schedulers) {

    override fun buildUseCaseObservable(params: MerchantPayRequest): Observable<StatusDTO> {
        val subMsisdn = if (sessionRepository.selectedMsisdn != sessionRepository.msisdn) sessionRepository.selectedMsisdn else null
        return restApi.merchantPay(subMsisdn,params)
    }
}