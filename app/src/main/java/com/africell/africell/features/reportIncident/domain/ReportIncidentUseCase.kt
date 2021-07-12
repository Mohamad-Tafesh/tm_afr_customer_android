package com.africell.africell.features.reportIncident.domain

import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi
import com.africell.africell.data.api.dto.StatusDTO
import com.africell.africell.data.api.requests.IncidentRequest
import io.reactivex.Observable
import javax.inject.Inject

class ReportIncidentUseCase
@Inject constructor(
    private val restApi: RestApi,
    schedulers: ExecutionSchedulers
) : UseCase<StatusDTO, ReportIncidentUseCase.Params>(schedulers) {

    override fun buildUseCaseObservable(params: Params): Observable<StatusDTO> {
        return restApi.sendIncident(IncidentRequest(params.categoryId,params.categoryName,params.message))
    }

    data class Params(
        val categoryId: Long?,
        val categoryName:String?,
        val message: String
    )
}