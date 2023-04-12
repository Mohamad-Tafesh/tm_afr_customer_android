package com.tedmob.afrimoney.app.usecase

import androidx.lifecycle.MutableLiveData
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.data.Resource
import com.tedmob.afrimoney.exception.AppException
import com.tedmob.afrimoney.exception.AppExceptionFactory
import io.reactivex.disposables.Disposable

abstract class RxResourceUseCaseExecutor<T: Any, in Params, Observer : Disposable, UseCase : RxUseCase<T, Params, *, Observer>>(
    useCase: UseCase,
    params: Params,
    protected val liveData: MutableLiveData<Resource<T>>,
    protected val appExceptionFactory: AppExceptionFactory,
    protected val appSessionNavigator: AppSessionNavigator? = null,
    protected val action: (() -> Unit)? = null
) : RxUseCaseExecutor<T, Params, Observer, UseCase>(useCase, params) {

    override fun preExecute() {
        liveData.value = Resource.Loading()
    }

    protected open fun onSuccess(result: T) {
        liveData.value = Resource.Success(result)
    }

    protected open fun onError(e: AppException) {
        liveData.value = Resource.Error(e.userMessage, action)
    }

    protected open fun afterSuccess(result: T) {
    }

    protected open fun afterError(e: AppException) {
    }
}

open class ObservableResourceUseCaseExecutor<T: Any, Params>(
    useCase: ObservableUseCase<T, Params>,
    params: Params,
    liveData: MutableLiveData<Resource<T>>,
    appExceptionFactory: AppExceptionFactory,
    appSessionNavigator: AppSessionNavigator? = null,
    action: (() -> Unit)? = null
) : RxResourceUseCaseExecutor<T, Params, DefaultObserver<T>, ObservableUseCase<T, Params>>(
    useCase,
    params,
    liveData,
    appExceptionFactory,
    appSessionNavigator,
    action
) {

    override fun getObserver(): DefaultObserver<T> {
        return object : DefaultObserver<T>(appExceptionFactory, appSessionNavigator) {
            override fun onNext(t: T) {
                this@ObservableResourceUseCaseExecutor.onSuccess(t)
                afterSuccess(t)
            }

            override fun onError(e: AppException) {
                this@ObservableResourceUseCaseExecutor.onError(e)
                afterError(e)
            }
        }
    }
}

open class SingleResourceUseCaseExecutor<T: Any, Params>(
    useCase: SingleUseCase<T, Params>,
    params: Params,
    liveData: MutableLiveData<Resource<T>>,
    appExceptionFactory: AppExceptionFactory,
    appSessionNavigator: AppSessionNavigator? = null,
    action: (() -> Unit)? = null
) : RxResourceUseCaseExecutor<T, Params, DefaultSingleObserver<T>, SingleUseCase<T, Params>>(
    useCase,
    params,
    liveData,
    appExceptionFactory,
    appSessionNavigator,
    action
) {

    override fun getObserver(): DefaultSingleObserver<T> {
        return object : DefaultSingleObserver<T>(appExceptionFactory, appSessionNavigator) {
            override fun onSuccess(t: T) {
                this@SingleResourceUseCaseExecutor.onSuccess(t)
                afterSuccess(t)
            }

            override fun onError(e: AppException) {
                this@SingleResourceUseCaseExecutor.onError(e)
                afterError(e)
            }
        }
    }
}