package com.africell.africell.app

import com.africell.africell.exception.AppException
import com.africell.africell.exception.AppExceptionFactory
import io.reactivex.observers.DisposableObserver

/**
 * Default [DisposableObserver] base class to be used whenever you want default error handling.
 */
abstract class DefaultObserver<T>(
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator,
) : DisposableObserver<T>() {

    override fun onComplete() {
        // no-op by default.

    }

    override fun onError(t: Throwable) {
        val appException = appExceptionFactory.make(t)
        when (appException.message) {
           "Invalid_token" -> onInvalidToken(appException)
            "Expired_token" -> appSessionNavigator.refreshToken()
            else -> onError(appException)
        }
    }

    abstract fun onError(e: AppException)

    open fun onInvalidToken(e: AppException) {
        appSessionNavigator?.invalidateSessionAndRestart(e)
    }
}
