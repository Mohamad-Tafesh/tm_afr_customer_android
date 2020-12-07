package com.tedmob.africell.features.accountInfo.domain


import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi
import com.tedmob.africell.data.api.dto.AboutDTO
import com.tedmob.africell.data.api.dto.AccountBalanceDTO
import com.tedmob.africell.features.profile.domain.GetProfileUseCase
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class GetAccountInfoUseCase
@Inject constructor(
    private val restApi: RestApi,
    private val getProfileUseCase: GetProfileUseCase,
    schedulers: ExecutionSchedulers
) : UseCase<AccountBalanceDTO, String>(schedulers) {

    override fun buildUseCaseObservable(params: String): Observable<AccountBalanceDTO> {
        return Observable.zip(
            getProfileUseCase.buildUseCaseObservable(Unit),
        restApi.getAccountBalance(params),
            BiFunction { t1, t2 ->  t2}
        )
    }

}