package com.africell.africell.features.bundles.domain


import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi

import com.africell.africell.data.api.dto.BundleCategoriesDTO
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