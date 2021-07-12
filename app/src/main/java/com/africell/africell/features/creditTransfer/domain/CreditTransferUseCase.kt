package com.africell.africell.features.creditTransfer.domain

import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi
import com.africell.africell.data.api.dto.SMSCountDTO
import com.africell.africell.data.repository.domain.SessionRepository
import io.reactivex.Observable
import javax.inject.Inject

class CreditTransferUseCase
@Inject constructor(
    private val restApi: RestApi,
    private val sessionRepository: SessionRepository,
    schedulers: ExecutionSchedulers
) : UseCase<SMSCountDTO, CreditTransferUseCase.Params>(schedulers) {

    override fun buildUseCaseObservable(params: Params): Observable<SMSCountDTO> {
        val subMsisdn = if (params.senderMsisdn!= sessionRepository.msisdn) params.senderMsisdn else null
        return restApi.creditTransfer(subMsisdn,params.receiverMsisdn,params.amount)
    }

    data class Params(
        val senderMsisdn:String?,
        val receiverMsisdn: String?,
        val amount: String
    )

}