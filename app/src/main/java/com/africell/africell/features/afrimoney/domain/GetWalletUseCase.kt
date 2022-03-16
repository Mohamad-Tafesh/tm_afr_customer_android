package com.africell.africell.features.afrimoney.domain


import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi
import com.africell.africell.data.api.dto.WalletDTO
import com.africell.africell.data.repository.domain.SessionRepository
import com.africell.africell.features.accountInfo.domain.GetSubAccountUseCase
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class GetWalletUseCase
@Inject constructor(
    private val restApi: RestApi,
    private val subAccountUseCase: GetSubAccountUseCase,
    private val sessionRepository: SessionRepository,
    schedulers: ExecutionSchedulers
) : UseCase<List<WalletDTO>, Unit>(schedulers) {

    override fun buildUseCaseObservable(params: Unit): Observable<List<WalletDTO>> {
        return restApi.getWallets()
    }

}