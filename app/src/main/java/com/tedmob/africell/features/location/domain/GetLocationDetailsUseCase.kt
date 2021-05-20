package com.tedmob.africell.features.location.domain


import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi
import com.tedmob.africell.data.api.dto.LocationDTO

import com.tedmob.africell.data.repository.domain.SessionRepository
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