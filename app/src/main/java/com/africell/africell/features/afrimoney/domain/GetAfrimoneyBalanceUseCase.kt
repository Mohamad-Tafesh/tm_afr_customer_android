package com.africell.africell.features.afrimoney.domain


import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi
import com.africell.africell.data.api.dto.AccountBalanceDTO
import com.africell.africell.data.api.dto.MoneyTransferBalanceDTO
import com.africell.africell.data.repository.domain.SessionRepository
import com.africell.africell.features.profile.domain.GetProfileUseCase
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class GetAfrimoneyBalanceUseCase
@Inject constructor(
    private val restApi: RestApi,
    private val sessionRepository: SessionRepository,
    private val getProfileUseCase: GetProfileUseCase,
    schedulers: ExecutionSchedulers
) : UseCase<List<MoneyTransferBalanceDTO>, String?>(schedulers) {

    override fun buildUseCaseObservable(params: String?): Observable<List<MoneyTransferBalanceDTO>> {
       val subMsisdn = if (sessionRepository.selectedMsisdn != sessionRepository.msisdn) sessionRepository.selectedMsisdn else null
        return restApi.getMMBallance(subMsisdn)
    }

}