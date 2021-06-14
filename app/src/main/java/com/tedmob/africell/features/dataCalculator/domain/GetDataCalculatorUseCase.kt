package com.tedmob.africell.features.dataCalculator.domain

import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi
import com.tedmob.africell.data.api.dto.DataCalculatorDTO
import com.tedmob.africell.data.repository.domain.SessionRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetDataCalculatorUseCase
@Inject constructor(
    private val restApi: RestApi,
    private val sessionRepository: SessionRepository,
    schedulers: ExecutionSchedulers
) : UseCase<DataCalculatorDTO,Unit>(schedulers) {

    override fun buildUseCaseObservable(params: Unit): Observable<DataCalculatorDTO> {
           return restApi.getDataCalculator()
    }
}