package com.tedmob.africell.features.aboutus.domain


import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi
import com.tedmob.africell.data.api.dto.AboutDTO
import io.reactivex.Observable
import javax.inject.Inject

class AboutUsUseCase
@Inject constructor(
    private val restApi: RestApi,
    schedulers: ExecutionSchedulers
) : UseCase<AboutDTO, Unit>(schedulers) {

    override fun buildUseCaseObservable(params: Unit): Observable<AboutDTO> {
        return /*restApi.aboutUs()*/Observable.just(AboutDTO(1212,"djskajda","test@t.com","","",
        123,"","","","","","",""))
    }

}