package com.tedmob.africell.features.reportIncident.domain

import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi
import com.tedmob.africell.data.api.dto.IncidentTypeDTO
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
