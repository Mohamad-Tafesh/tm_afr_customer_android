package com.tedmob.africell.features.accountsNumber.domain


import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi
import com.tedmob.africell.data.api.dto.AboutDTO
import com.tedmob.africell.data.api.dto.SubAccountDTO
import com.tedmob.africell.data.entity.SubAccount
import io.reactivex.Observable
import javax.inject.Inject

class DeleteAccountUseCase
@Inject constructor(
    private val restApi: RestApi,
    schedulers: ExecutionSchedulers
) : UseCase<List<SubAccount>, String>(schedulers) {

    override fun buildUseCaseObservable(params: String): Observable<List<SubAccount>> {
        return restApi.deleteSubAccount(params).map {
            val list =  mutableListOf<SubAccount>()
            it.mainAccount?.let { list.add(SubAccount(it,true))}
            it.subAccount?.forEach { list.add(SubAccount(it,false)) }
            list
        }
    }

}