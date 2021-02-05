package com.tedmob.africell.features.accountInfo.domain


import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi
import com.tedmob.africell.data.api.dto.AboutDTO
import com.tedmob.africell.data.api.dto.SubAccountDTO
import com.tedmob.africell.data.entity.SubAccount
import io.reactivex.Observable
import javax.inject.Inject

class GetSubAccountUseCase
@Inject constructor(
    private val restApi: RestApi,
    schedulers: ExecutionSchedulers
) : UseCase<List<SubAccount>, Unit>(schedulers) {

    override fun buildUseCaseObservable(params: Unit): Observable<List<SubAccount>> {
        return restApi.getSubAccount().map {
            val list =  mutableListOf<SubAccount>()
            it.mainAccount?.let { list.add(SubAccount(it,true))}
            it.subAccount?.forEach { list.add(SubAccount(it,false)) }
            list
        }
    }

}