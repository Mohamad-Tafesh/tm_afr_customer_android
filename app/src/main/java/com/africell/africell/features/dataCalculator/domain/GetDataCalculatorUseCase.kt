package com.africell.africell.features.dataCalculator.domain

import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi
import com.africell.africell.data.api.dto.DataCalculatorDTO
import com.africell.africell.data.repository.domain.SessionRepository
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