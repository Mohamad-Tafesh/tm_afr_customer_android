package com.tedmob.africell.features.bundles.domain


import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi
import com.tedmob.africell.data.api.dto.BundlesDTO
import io.reactivex.Observable
import javax.inject.Inject

class GetBundlesByCategoryUseCase
@Inject constructor(
    private val restApi: RestApi,
    schedulers: ExecutionSchedulers
) : UseCase<List<BundlesDTO>, GetBundlesByCategoryUseCase.Params>(schedulers) {

    override fun buildUseCaseObservable(params: Params): Observable<List<BundlesDTO>> {
        return restApi.getBundlesByCategories(params.categoryId, params.searchBundle)
    }

    data class Params(
        val categoryId: Long?,
        val searchBundle: String?
    )
}