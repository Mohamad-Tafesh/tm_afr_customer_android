package com.africell.africell.features.home.domain

import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi
import com.africell.africell.data.repository.domain.SessionRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetImagesUseCase
@Inject constructor(
    private val restApi: RestApi,
    private val session: SessionRepository,
    schedulers: ExecutionSchedulers
) : UseCase<List<String>, GetImagesUseCase.Params>(schedulers) {


    override fun buildUseCaseObservable(params: Params): Observable<List<String>> {
        return restApi.getImagesURls(params.imageType,params.pageName)
        }


    data class Params(
        val imageType: String?,
        val pageName: String?
    )
}
