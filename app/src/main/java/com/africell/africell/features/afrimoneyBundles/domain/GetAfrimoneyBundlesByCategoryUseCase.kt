package com.africell.africell.features.afrimoneyBundles.domain


import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi
import com.africell.africell.data.api.dto.BundlesDTO
import io.reactivex.Observable
import javax.inject.Inject

class GetAfrimoneyBundlesByCategoryUseCase
@Inject constructor(
    private val restApi: RestApi,
    schedulers: ExecutionSchedulers
) : UseCase<List<BundlesDTO>, GetAfrimoneyBundlesByCategoryUseCase.Params>(schedulers) {

    override fun buildUseCaseObservable(params: Params): Observable<List<BundlesDTO>> {
        return restApi.getAfriMoneyBundlesByCategories(params.categoryId, params.searchBundle)
    }

    data class Params(
        val categoryId: String,
        val searchBundle: String?
    )
}