package com.tedmob.africell.features.customerCare.domain

import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi
import com.tedmob.africell.data.api.dto.StatusDTO
import io.reactivex.Observable
import javax.inject.Inject

class SubmitCustomerCareUseCase
@Inject constructor(
    private val restApi: RestApi,
    schedulers: ExecutionSchedulers
) : UseCase<StatusDTO, SubmitCustomerCareUseCase.Params>(schedulers) {

    override fun buildUseCaseObservable(params: Params): Observable<StatusDTO> {
          /*restApi.(params.firstName,params.lastName, params.email,params.phoneNumber,params.categoryId,params.message)*/
        return Observable.just(StatusDTO("sk"))
    }

    data class Params(
        val email: String?,
        val categoryId: Long?,
        val message: String
    )

}