package com.tedmob.africell.features.services.domain


import com.tedmob.africell.data.api.dto.LocationDTO
import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi
import com.tedmob.africell.data.api.dto.ServicesDTO
import io.reactivex.Observable
import javax.inject.Inject

class GetServiceDetailsUseCase
@Inject constructor(
        private val restApi: RestApi,
        schedulers: ExecutionSchedulers)
    : UseCase<ServicesDTO, GetServiceDetailsUseCase.Params>(schedulers) {

    override fun buildUseCaseObservable(params: Params): Observable<ServicesDTO> {
        return  restApi.getVasServiceDetails(params.sname)
}

    data class Params(val sname: String?)
}