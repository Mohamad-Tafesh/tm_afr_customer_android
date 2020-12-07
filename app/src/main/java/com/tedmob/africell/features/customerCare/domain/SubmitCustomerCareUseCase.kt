package com.tedmob.africell.features.customerCare.domain

import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi
import com.tedmob.africell.data.api.dto.StatusDTO
import com.tedmob.africell.data.api.requests.HelpRequest
import io.reactivex.Observable
import javax.inject.Inject

class SubmitCustomerCareUseCase
@Inject constructor(
    private val restApi: RestApi,
    schedulers: ExecutionSchedulers
) : UseCase<StatusDTO, SubmitCustomerCareUseCase.Params>(schedulers) {

    override fun buildUseCaseObservable(params: Params): Observable<StatusDTO> {
          return restApi.sendHelpMessage(HelpRequest(params.categoryId,params.categoryName,params.message))
    }

    data class Params(
        val categoryId: Long?,
        val categoryName:String?,
        val message: String
    )

}