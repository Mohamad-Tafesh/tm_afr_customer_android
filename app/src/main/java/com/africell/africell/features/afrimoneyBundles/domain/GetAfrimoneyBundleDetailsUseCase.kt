package com.africell.africell.features.afrimoneyBundles.domain


import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi
import com.africell.africell.data.api.dto.BundleInfo

import com.africell.africell.data.repository.domain.SessionRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetAfrimoneyBundleDetailsUseCase
@Inject constructor(
        private val restApi: RestApi,
        private val sessionRepository: SessionRepository,
        schedulers: ExecutionSchedulers)
    : UseCase<BundleInfo, GetAfrimoneyBundleDetailsUseCase.Params>(schedulers) {

    override fun buildUseCaseObservable(params: Params): Observable<BundleInfo> {

        return restApi.getAfriMoneyBundleDetails(params.bundleId)
    }
    data class Params(val bundleId: String)
}