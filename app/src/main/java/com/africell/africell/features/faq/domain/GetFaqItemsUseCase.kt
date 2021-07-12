package com.africell.africell.features.faq.domain

import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi
import com.africell.africell.data.repository.domain.SessionRepository
import com.africell.africell.features.faq.FaqItem
import io.reactivex.Observable
import javax.inject.Inject

class GetFaqItemsUseCase
@Inject constructor(
    private val api: RestApi,
    private val session: SessionRepository,
    schedulers: ExecutionSchedulers
) : UseCase<List<FaqItem>, Unit>(schedulers) {

    override fun buildUseCaseObservable(params: Unit): Observable<List<FaqItem>> {
        return api.getFaqs()
    }
}