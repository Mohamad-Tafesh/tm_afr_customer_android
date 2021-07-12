package com.africell.africell.features.aboutus.domain


import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi
import com.africell.africell.data.api.dto.AboutDTO
import io.reactivex.Observable
import javax.inject.Inject

class AboutUsUseCase
@Inject constructor(
    private val restApi: RestApi,
    schedulers: ExecutionSchedulers
) : UseCase<AboutDTO, Unit>(schedulers) {

    override fun buildUseCaseObservable(params: Unit): Observable<AboutDTO> {
        return restApi.aboutUs()
    }

}