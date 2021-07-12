package com.africell.africell.features.lineRecharge.domain

import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi
import com.africell.africell.data.api.dto.SMSCountDTO
import com.africell.africell.data.repository.domain.SessionRepository
import io.reactivex.Observable
import javax.inject.Inject

class VoucherRechargeUseCase
@Inject constructor(
    private val restApi: RestApi,
    private val sessionRepository: SessionRepository,
    schedulers: ExecutionSchedulers
) : UseCase<SMSCountDTO, VoucherRechargeUseCase.Params>(schedulers) {

    override fun buildUseCaseObservable(params: Params): Observable<SMSCountDTO> {
        val subMsisdn = if (sessionRepository.selectedMsisdn != sessionRepository.msisdn) sessionRepository.selectedMsisdn else null
        return restApi.voucherRecharge(subMsisdn,params.receiverMsisdn,params.voucherNumber)
    }

    data class Params(
        val receiverMsisdn: String?,
        val voucherNumber: String
    )

}