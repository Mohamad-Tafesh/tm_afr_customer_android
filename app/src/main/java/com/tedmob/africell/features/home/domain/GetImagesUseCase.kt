package com.tedmob.africell.features.home.domain

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi
import com.tedmob.africell.data.api.dto.SubAccountDTO
import com.tedmob.africell.data.api.requests.VerifyOTPRequest
import com.tedmob.africell.data.repository.domain.SessionRepository
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
