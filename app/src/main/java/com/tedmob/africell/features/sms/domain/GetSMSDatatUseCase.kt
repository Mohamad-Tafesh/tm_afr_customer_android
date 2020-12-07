package com.tedmob.africell.features.sms.domain


import com.tedmob.africell.data.api.dto.LocationDTO
import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi
import com.tedmob.africell.data.api.dto.BookNumberDTO
import com.tedmob.africell.data.api.dto.SMSCountDTO
import com.tedmob.africell.data.entity.SMSData
import com.tedmob.africell.data.repository.domain.SessionRepository
import com.tedmob.africell.features.authentication.domain.GetCountriesUseCase
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class GetSMSDatatUseCase
@Inject constructor(
        private val restApi: RestApi,
        private val getCountriesUseCase: GetCountriesUseCase,
        private val sessionRepository: SessionRepository,
        schedulers: ExecutionSchedulers)
    : UseCase<SMSData, Unit>(schedulers) {

    override fun buildUseCaseObservable(params: Unit): Observable<SMSData> {
        return Observable.zip(restApi.getSMSCount(sessionRepository.msisdn),
            getCountriesUseCase.buildUseCaseObservable(params),
            BiFunction { smsCount, countries -> SMSData(countries,smsCount ) }
        )

    }


}