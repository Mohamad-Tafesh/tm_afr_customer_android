package com.tedmob.africell.features.usefulNumber.domain


import com.tedmob.africell.data.api.dto.LocationDTO
import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi
import com.tedmob.africell.data.api.dto.ServicesDTO
import com.tedmob.africell.data.api.dto.UsefulNumberDTO
import io.reactivex.Observable
import javax.inject.Inject

class GetUsefulUseCase
@Inject constructor(
        private val restApi: RestApi,
        schedulers: ExecutionSchedulers)
    : UseCase<List<UsefulNumberDTO>, Unit>(schedulers) {

    override fun buildUseCaseObservable(params: Unit): Observable<List<UsefulNumberDTO>> {

        return Observable.just(mutableListOf(UsefulNumberDTO(1, "dsa",false),
            UsefulNumberDTO(1, "dsa",false)))
    }

}