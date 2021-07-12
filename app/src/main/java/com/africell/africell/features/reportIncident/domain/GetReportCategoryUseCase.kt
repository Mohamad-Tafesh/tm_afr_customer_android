package com.africell.africell.features.reportIncident.domain

import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi
import com.africell.africell.data.api.dto.IncidentTypeDTO
import io.reactivex.Observable
import javax.inject.Inject

class GetReportCategoryUseCase
@Inject constructor(
    private val restApi: RestApi,
    schedulers: ExecutionSchedulers
) : UseCase<List<IncidentTypeDTO>, Unit>(schedulers) {

    override fun buildUseCaseObservable(params: Unit): Observable<List<IncidentTypeDTO>> {
        return restApi.getIncidentType()
    }
}
