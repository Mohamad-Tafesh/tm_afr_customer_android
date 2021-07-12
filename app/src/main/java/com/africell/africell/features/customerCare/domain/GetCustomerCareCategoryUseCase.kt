package com.africell.africell.features.customerCare.domain

import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi
import com.africell.africell.data.api.dto.SupportCategoryDTO
import io.reactivex.Observable
import javax.inject.Inject

class GetCustomerCareCategoryUseCase
@Inject constructor(
    private val restApi: RestApi,
    schedulers: ExecutionSchedulers
) : UseCase<List<SupportCategoryDTO>, Unit>(schedulers) {

    override fun buildUseCaseObservable(params: Unit): Observable<List<SupportCategoryDTO>> {
        return restApi.getSupportCategory()
    }
}
