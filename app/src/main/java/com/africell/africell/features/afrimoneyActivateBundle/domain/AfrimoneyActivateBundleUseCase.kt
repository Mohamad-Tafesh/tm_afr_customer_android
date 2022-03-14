package com.africell.africell.features.afrimoneyActivateBundle.domain


import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi

import com.africell.africell.data.api.dto.StatusDTO
import com.africell.africell.data.api.requests.ActivateBundleRequest
import io.reactivex.Observable
import javax.inject.Inject

class AfrimoneyActivateBundleUseCase
@Inject constructor(
        private val restApi: RestApi,
        schedulers: ExecutionSchedulers)
    : UseCase<StatusDTO, ActivateBundleRequest>(schedulers) {

    override fun buildUseCaseObservable(params: ActivateBundleRequest): Observable<StatusDTO> {
        return restApi.activateBundle(params)
    }
}