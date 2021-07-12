package com.africell.africell.features.location.domain


import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi
import com.africell.africell.data.api.dto.LocationDTO

import com.africell.africell.data.repository.domain.SessionRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetLocationDetailsUseCase
@Inject constructor(
        private val restApi: RestApi,
        private val sessionRepository: SessionRepository,
        schedulers: ExecutionSchedulers)
    : UseCase<LocationDTO, GetLocationDetailsUseCase.Params>(schedulers) {

    override fun buildUseCaseObservable(params: Params): Observable<LocationDTO> {

        return restApi.getShopLocationDetails(params.id)
    }
    data class Params(val id: String?)
}