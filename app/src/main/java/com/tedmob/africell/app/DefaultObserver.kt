package com.tedmob.africell.app

import com.tedmob.africell.exception.AppException
import com.tedmob.africell.exception.AppExceptionFactory
import io.reactivex.observers.DisposableObserver

/**
 * Default [DisposableObserver] base class to be used whenever you want default error handling.
 */
abstract class DefaultObserver<T>(
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator? = null
) : DisposableObserver<T>() {

    override fun onComplete() {
        // no-op by default.
    }

    override fun onError(t: Throwable) {
        val appException = appExceptionFactory.make(t)
        if (appException.code == AppException.Code.INVALID_TOKEN && appSessionNavigator != null) {
            onInvalidToken(appException)
        } else {
            onError(appException)
        }
    }

    abstract fun onError(e: AppException)

    open fun onInvalidToken(e: AppException) {
        appSessionNavigator?.invalidateSessionAndRestart(e)
    }
}
