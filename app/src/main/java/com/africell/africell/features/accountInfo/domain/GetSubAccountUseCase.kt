package com.africell.africell.features.accountInfo.domain


import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi
import com.africell.africell.data.entity.SubAccount
import com.africell.africell.data.repository.domain.SessionRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetSubAccountUseCase
@Inject constructor(
    private val restApi: RestApi,
    private val sessionRepository: SessionRepository,
    schedulers: ExecutionSchedulers
) : UseCase<List<SubAccount>, Unit>(schedulers) {

    override fun buildUseCaseObservable(params: Unit): Observable<List<SubAccount>> {
        return restApi.getSubAccount().map {
            val list =  mutableListOf<SubAccount>()
            it.mainAccount?.let {
                sessionRepository.msisdn= it
            }
            it.mainAccount?.let { list.add(SubAccount(it,true))}
            it.subAccount?.forEach { list.add(SubAccount(it,false)) }
            list
        }
    }

}