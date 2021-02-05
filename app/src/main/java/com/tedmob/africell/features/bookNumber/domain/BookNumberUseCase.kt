package com.tedmob.africell.features.bookNumber.domain


import com.tedmob.africell.data.api.dto.LocationDTO
import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi
import com.tedmob.africell.data.api.dto.StatusDTO

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