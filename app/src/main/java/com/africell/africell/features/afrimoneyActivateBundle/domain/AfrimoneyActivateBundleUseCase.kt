package com.africell.africell.features.afrimoneyActivateBundle.domain


import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi

import com.africell.africell.data.api.dto.StatusDTO
import com.africell.africell.data.api.requests.ActivateBundleRequest
import com.africell.africell.data.api.requests.afrimoney.AfrimoneyActivateBundleRequest
import io.reactivex.Observable
import javax.inject.Inject

class AfrimoneyActivateBundleUseCase
@Inject constructor(
        private val restApi: RestApi,
        schedulers: ExecutionSchedulers)
    : UseCase<StatusDTO, AfrimoneyActivateBundleRequest>(schedulers) {

    override fun buildUseCaseObservable(params: AfrimoneyActivateBundleRequest): Observable<StatusDTO> {
        return restApi.afrimoneyActivateBundle(params)
    }
}