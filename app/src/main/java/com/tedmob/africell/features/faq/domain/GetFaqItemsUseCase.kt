package com.tedmob.africell.features.faq.domain

import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi
import com.tedmob.africell.data.repository.domain.SessionRepository
import com.tedmob.africell.features.faq.FaqItem
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