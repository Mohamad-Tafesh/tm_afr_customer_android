package com.tedmob.africell.features.location.domain


import com.tedmob.africell.data.api.dto.LocationDTO
import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi
import io.reactivex.Observable
import javax.inject.Inject

class GetLocationsUseCase
@Inject constructor(
        private val restApi: RestApi,
        schedulers: ExecutionSchedulers)
    : UseCase<List<LocationDTO>, GetLocationsUseCase.Params>(schedulers) {

    override fun buildUseCaseObservable(params: Params): Observable<List<LocationDTO>> {

        /*restApi.getLocations(params.search ,params.regionId,params.lat,params.long)  }*/
        return Observable.just(mutableListOf(LocationDTO(1, "dsa", "s", null, null, 12.4, "", "")))
    }

    data class Params(val search:String?, val lat:Double?,val long:Double?)
}