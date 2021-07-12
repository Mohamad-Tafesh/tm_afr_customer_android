package com.africell.africell.features.location.domain


import com.africell.africell.data.api.dto.LocationDTO
import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi
import io.reactivex.Observable
import javax.inject.Inject

class GetLocationsUseCase
@Inject constructor(
        private val restApi: RestApi,
        schedulers: ExecutionSchedulers)
    : UseCase<List<LocationDTO>, GetLocationsUseCase.Params>(schedulers) {

    override fun buildUseCaseObservable(params: Params): Observable<List<LocationDTO>> {


        return  restApi.getShopLocation(params.search)
}

    data class Params(val search:String?, val lat:Double?,val long:Double?)
}