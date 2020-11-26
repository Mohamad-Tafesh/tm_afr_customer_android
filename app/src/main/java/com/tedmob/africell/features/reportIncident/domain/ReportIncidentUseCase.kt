package com.tedmob.africell.features.reportIncident.domain

import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi
import com.tedmob.africell.data.api.dto.StatusDTO
import io.reactivex.Observable
import javax.inject.Inject

class ReportIncidentUseCase
@Inject constructor(
    private val restApi: RestApi,
    schedulers: ExecutionSchedulers
) : UseCase<StatusDTO, ReportIncidentUseCase.ContactInfo>(schedulers) {

    override fun buildUseCaseObservable(params: ContactInfo): Observable<StatusDTO> {
          /*restApi.(params.firstName,params.lastName, params.email,params.phoneNumber,params.categoryId,params.message)*/
        return Observable.just(StatusDTO("sk"))
    }

    data class ContactInfo(
        val email: String?,
        val categoryId: Long?,
        val message: String
    )

}