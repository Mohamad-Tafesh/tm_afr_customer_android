package com.africell.africell.features.usefulNumber.domain


import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi
import com.africell.africell.data.api.dto.UsefulNumberDTO
import io.reactivex.Observable
import javax.inject.Inject

class GetUsefulUseCase
@Inject constructor(
        private val restApi: RestApi,
        schedulers: ExecutionSchedulers)
    : UseCase<List<UsefulNumberDTO>, Unit>(schedulers) {

    override fun buildUseCaseObservable(params: Unit): Observable<List<UsefulNumberDTO>> {

        return restApi.getUsefullNumber()
    }

}