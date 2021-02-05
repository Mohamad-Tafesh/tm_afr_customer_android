package com.tedmob.africell.features.services.domain


import com.tedmob.africell.data.api.dto.LocationDTO
import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi
import com.tedmob.africell.data.api.dto.ServicesDTO
import com.tedmob.africell.data.repository.domain.SessionRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetServicesUseCase
@Inject constructor(
        private val restApi: RestApi,
        private val sessionRepository: SessionRepository,
        schedulers: ExecutionSchedulers)
    : UseCase<List<ServicesDTO>, Unit>(schedulers) {

    override fun buildUseCaseObservable(params: Unit): Observable<List<ServicesDTO>> {
        val subMsisdn = if (sessionRepository.selectedMsisdn != sessionRepository.msisdn) sessionRepository.selectedMsisdn else ""
        return restApi.getMyServices(subMsisdn)
    }

}