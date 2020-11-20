package com.tedmob.africell.app

import androidx.lifecycle.MutableLiveData
import com.tedmob.africell.data.Resource
import com.tedmob.africell.exception.AppException
import com.tedmob.africell.exception.AppExceptionFactory
import io.reactivex.observers.DisposableObserver

/**
 * Default [DisposableObserver] base class to be used whenever you want default error handling for a [Resource]
 */
abstract class ResourceObserver<T>(
    private val liveData: MutableLiveData<Resource<T>>,
    appExceptionFactory: AppExceptionFactory,
    private val action: (() -> Unit)? = null
) : DefaultObserver<T>(appExceptionFactory) {

    final override fun onNext(t: T) {
        liveData.value = Resource.Success(t)
        afterSuccess(t)
    }

    final override fun onError(e: AppException) {
        liveData.value = Resource.Error(e.userMessage, action)
        afterError(e)
    }

    open fun afterSuccess(t: T) {

    }

    open fun afterError(e: AppException) {

    }
}
