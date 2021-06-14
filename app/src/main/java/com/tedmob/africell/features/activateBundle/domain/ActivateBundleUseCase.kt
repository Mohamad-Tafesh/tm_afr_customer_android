package com.tedmob.africell.features.activateBundle.domain


import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi

import com.tedmob.africell.data.api.dto.StatusDTO
import com.tedmob.africell.data.api.requests.ActivateBundleRequest
import io.reactivex.Observable
import javax.inject.Inject

class ActivateBundleUseCase
@Inject constructor(
        private val restApi: RestApi,
        schedulers: ExecutionSchedulers)
    : UseCase<StatusDTO, ActivateBundleRequest>(schedulers) {

    override fun buildUseCaseObservable(params: ActivateBundleRequest): Observable<StatusDTO> {
        return restApi.activateBundle(params)
    }
}