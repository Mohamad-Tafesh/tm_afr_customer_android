package com.africell.africell.features.accountInfo.domain


import com.africell.africell.BuildConfig
import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi
import com.africell.africell.data.entity.SubAccount
import com.africell.africell.data.repository.domain.SessionRepository
import com.africell.africell.exception.AppException
import com.africell.africell.exception.AppExceptionFactory
import com.africell.africell.features.authentication.domain.RefreshTokenUseCase
import io.reactivex.Observable
import javax.inject.Inject

class GetSubAccountUseCase
@Inject constructor(
    private val restApi: RestApi,
    private val sessionRepository: SessionRepository,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    schedulers: ExecutionSchedulers
) : UseCase<List<SubAccount>, Unit>(schedulers) {

    override fun buildUseCaseObservable(params: Unit): Observable<List<SubAccount>> {
        return refreshTokenIfNeeded { restApi.getSubAccount() }.map {
            val list = mutableListOf<SubAccount>()
            it.mainAccount?.let {
                sessionRepository.msisdn = it
            }
            it.mainAccount?.let { list.add(SubAccount(it, true)) }
            it.subAccount?.forEach { list.add(SubAccount(it, false)) }
            list
        }
    }


    fun <T> refreshTokenIfNeeded(
        observableProvider: () -> Observable<T>,
    ): Observable<T> {
        return observableProvider()
//            .switchMap {
//                if (BuildConfig.DEBUG) {
//                    Observable.error(AppException(500, "Expired_token", "Expired_token"))
//                } else {
//                    Observable.just(it)
//                }
//            }
            .onErrorResumeNext { t: Throwable ->
                val appException = appExceptionFactory.make(t)
                if (appException.userMessage == "Expired_token") {
                    refreshTokenUseCase.buildUseCaseObservable(Unit)
                        .flatMap {
                            observableProvider()
                        }
                } else {
                    Observable.error(appException)
                }

            }
    }
}