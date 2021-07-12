package com.africell.africell.features.terms.domain

import com.africell.africell.data.api.dto.TermsDTO
import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi
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
