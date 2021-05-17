package com.tedmob.africell.features.bundles.domain


import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi
import com.tedmob.africell.data.api.dto.BundleInfo
import com.tedmob.africell.data.api.dto.BundlesDTO

import com.tedmob.africell.data.api.dto.MyBundlesAndServices
import com.tedmob.africell.data.repository.domain.SessionRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetBundleDetailsUseCase
@Inject constructor(
        private val restApi: RestApi,
        private val sessionRepository: SessionRepository,
        schedulers: ExecutionSchedulers)
    : UseCase<BundleInfo, GetBundleDetailsUseCase.Params>(schedulers) {

    override fun buildUseCaseObservable(params: Params): Observable<BundleInfo> {

        return restApi.getBundleDetails(params.bundleId)
    }
    data class Params( val bundleId: Long?)
}