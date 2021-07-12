package com.africell.africell.features.sms.domain


import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi

import com.africell.africell.data.entity.SMSData
import com.africell.africell.data.repository.domain.SessionRepository
import com.africell.africell.features.authentication.domain.GetCountriesUseCase
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
        val subMsisdn = if (sessionRepository.selectedMsisdn != sessionRepository.msisdn) sessionRepository.selectedMsisdn else null

        return Observable.zip(restApi.getSMSCount(subMsisdn),
            getCountriesUseCase.buildUseCaseObservable(params),
            BiFunction { smsCount, countries -> SMSData(countries,smsCount ) }
        )

    }


}