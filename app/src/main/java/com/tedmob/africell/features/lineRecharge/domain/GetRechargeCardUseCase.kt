package com.tedmob.africell.features.lineRecharge.domain


import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi
import com.tedmob.africell.data.api.dto.RechargeCardDTO
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