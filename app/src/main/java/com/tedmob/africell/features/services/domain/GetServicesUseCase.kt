package com.tedmob.africell.features.services.domain


import com.tedmob.africell.data.api.dto.LocationDTO
import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi
import com.tedmob.africell.data.api.dto.ServicesDTO
import io.reactivex.Observable
import javax.inject.Inject

class GetServicesUseCase
@Inject constructor(
        private val restApi: RestApi,
        schedulers: ExecutionSchedulers)
    : UseCase<List<ServicesDTO>, Unit>(schedulers) {

    override fun buildUseCaseObservable(params: Unit): Observable<List<ServicesDTO>> {

        /*restApi.getLocations(params.search ,params.regionId,params.lat,params.long)  }*/
        return Observable.just(mutableListOf(ServicesDTO(1, "dsa",true),
            ServicesDTO(1, "dsa",false)))
    }

}