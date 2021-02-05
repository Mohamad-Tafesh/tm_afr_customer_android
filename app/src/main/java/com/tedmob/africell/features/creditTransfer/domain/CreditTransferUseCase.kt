package com.tedmob.africell.features.creditTransfer.domain

import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi
import com.tedmob.africell.data.api.dto.SMSCountDTO
import com.tedmob.africell.data.repository.domain.SessionRepository
import io.reactivex.Observable
import javax.inject.Inject

class CreditTransferUseCase
@Inject constructor(
    private val restApi: RestApi,
    private val sessionRepository: SessionRepository,
    schedulers: ExecutionSchedulers
) : UseCase<SMSCountDTO, CreditTransferUseCase.Params>(schedulers) {

    override fun buildUseCaseObservable(params: Params): Observable<SMSCountDTO> {
        val subMsisdn = if (sessionRepository.selectedMsisdn != sessionRepository.msisdn) sessionRepository.selectedMsisdn else ""
        return restApi.creditTransfer(subMsisdn,params.receiverMsisdn,params.amount)
    }

    data class Params(
        val receiverMsisdn: String?,
        val amount: String
    )

}