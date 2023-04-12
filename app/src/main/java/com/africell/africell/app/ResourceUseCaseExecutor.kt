package com.africell.africell.app

import androidx.lifecycle.MutableLiveData
import com.tedmob.afrimoney.data.Resource
import com.africell.africell.exception.AppException
import com.africell.africell.exception.AppExceptionFactory

/**
 * Used to execute a [UseCase] and return the result in a [MutableLiveData] of type [Resource].
 */
open class ResourceUseCaseExecutor<T, in Params>(
    useCase: UseCase<T, Params>,
    params: Params,
    private val liveData: MutableLiveData<Resource<T>>,
        private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator,
    private val action: (() -> Unit)? = null
) : UseCaseExecutor<T, Params>(useCase, params) {

    override fun preExecute() {
        liveData.value = Resource.Loading()
    }

    override fun getObserver(): DefaultObserver<T> {
        return object : DefaultObserver<T>(appExceptionFactory, appSessionNavigator) {
            override fun onNext(t: T) {
                liveData.value = Resource.Success(t)
                afterSuccess(t)
            }

            override fun onError(e: AppException) {
                liveData.value = Resource.Error(e.userMessage, action)

                afterError(e)
            }
        }
    }

    open fun afterSuccess(result: T) {

    }

    open fun afterError(e: AppException) {

    }
}