package com.africell.africell.features.lineRecharge.domain


import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi
import com.africell.africell.data.api.dto.RechargeCardDTO
import io.reactivex.Observable
import javax.inject.Inject

class GetRechargeCardUseCase
@Inject constructor(
        private val restApi: RestApi,
        schedulers: ExecutionSchedulers)
    : UseCase<List<RechargeCardDTO>, Unit>(schedulers) {

    override fun buildUseCaseObservable(params: Unit): Observable<List<RechargeCardDTO>> {
        return restApi.getAllRechargedCard()
    }

}