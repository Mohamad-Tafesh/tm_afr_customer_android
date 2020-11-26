package com.tedmob.africell.features.customerCare.domain

import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi
import com.tedmob.africell.data.api.dto.SupportCategoryDTO
import io.reactivex.Observable
import javax.inject.Inject

class GetCustomerCareCategoryUseCase
@Inject constructor(
    private val restApi: RestApi,
    schedulers: ExecutionSchedulers
) : UseCase<List<SupportCategoryDTO>, Unit>(schedulers) {

    override fun buildUseCaseObservable(params: Unit): Observable<List<SupportCategoryDTO>> {
        return /*restApi.getContactSubject()*/Observable.just(mutableListOf(SupportCategoryDTO(123,"1234")))
    }
}
