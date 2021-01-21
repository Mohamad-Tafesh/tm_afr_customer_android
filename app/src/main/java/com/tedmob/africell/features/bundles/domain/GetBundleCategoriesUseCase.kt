package com.tedmob.africell.features.bundles.domain


import com.tedmob.africell.data.api.dto.LocationDTO
import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi

import com.tedmob.africell.data.api.dto.BundleCategoriesDTO
import io.reactivex.Observable
import javax.inject.Inject

class GetBundleCategoriesUseCase
@Inject constructor(
        private val restApi: RestApi,
        schedulers: ExecutionSchedulers)
    : UseCase<List<BundleCategoriesDTO>, Unit>(schedulers) {

    override fun buildUseCaseObservable(params: Unit): Observable<List<BundleCategoriesDTO>> {
        return restApi.getBundlesCategories()
    }
}