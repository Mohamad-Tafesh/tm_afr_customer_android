package com.africell.africell.features.bookNumber.domain


import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi
import com.africell.africell.data.api.dto.StatusDTO

import io.reactivex.Observable
import javax.inject.Inject

class BookNumberUseCase
@Inject constructor(
        private val restApi: RestApi,
        schedulers: ExecutionSchedulers)
    : UseCase<StatusDTO, BookNumberUseCase.Params>(schedulers) {

    override fun buildUseCaseObservable(params: Params): Observable<StatusDTO> {
        return  restApi.bookNumber(params.number)
    }

    data class Params(val number:String?)
}