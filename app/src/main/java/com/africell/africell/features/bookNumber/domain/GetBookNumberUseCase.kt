package com.africell.africell.features.bookNumber.domain


import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi

import io.reactivex.Observable
import javax.inject.Inject

class GetBookNumberUseCase
@Inject constructor(
        private val restApi: RestApi,
        schedulers: ExecutionSchedulers)
    : UseCase<List<String>, GetBookNumberUseCase.Params>(schedulers) {

    override fun buildUseCaseObservable(params: Params): Observable<List<String>> {
        return  restApi.getFreeNumber(params.search)
    }

    data class Params(val search:String?)
}