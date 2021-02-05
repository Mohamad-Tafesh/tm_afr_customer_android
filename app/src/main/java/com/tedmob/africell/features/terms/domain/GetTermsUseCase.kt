package com.tedmob.africell.features.terms.domain

import com.tedmob.africell.data.api.dto.TermsDTO
import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi
import io.reactivex.Observable
import javax.inject.Inject

class GetTermsUseCase
@Inject constructor(
    private val restApi: RestApi,
    schedulers: ExecutionSchedulers
) : UseCase<TermsDTO, Unit>(schedulers) {

    override fun buildUseCaseObservable(params: Unit): Observable<TermsDTO> {
        return restApi.getTerms()
    }
}
