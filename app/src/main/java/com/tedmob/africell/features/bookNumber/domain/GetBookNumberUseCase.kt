package com.tedmob.africell.features.bookNumber.domain


import com.tedmob.africell.data.api.dto.LocationDTO
import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi
import com.tedmob.africell.data.api.dto.BookNumberDTO
import io.reactivex.Observable
import javax.inject.Inject

class GetBookNumberUseCase
@Inject constructor(
        private val restApi: RestApi,
        schedulers: ExecutionSchedulers)
    : UseCase<List<BookNumberDTO>, GetBookNumberUseCase.Params>(schedulers) {

    override fun buildUseCaseObservable(params: Params): Observable<List<BookNumberDTO>> {
        return Observable.just(mutableListOf(BookNumberDTO(1, "dsa", false)))
    }

    data class Params(val search:String?)
}