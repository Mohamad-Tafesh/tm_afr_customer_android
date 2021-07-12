package com.africell.africell.features.services.domain


import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi
import com.africell.africell.data.api.dto.ServicesDTO
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