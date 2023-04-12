package com.tedmob.afrimoney.app.usecase

import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.exception.AppException
import com.tedmob.afrimoney.exception.AppExceptionFactory
import io.reactivex.observers.DisposableSingleObserver

abstract class DefaultSingleObserver<T>(
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator? = null
) : DisposableSingleObserver<T>() {

    override fun onError(t: Throwable) {
        val appException = appExceptionFactory.make(t)
        if (appException.code == AppException.Code.INVALID_TOKEN && appSessionNavigator != null) {
            onInvalidToken(appException)
        } else if (appException.code == AppException.Code.INVALID_PIN && appSessionNavigator != null) {
            onInvalidPin(appException)
        } else {
            onError(appException)
        }
    }

    abstract fun onError(e: AppException)

    open fun onInvalidToken(e: AppException) {
        appSessionNavigator?.invalidateSessionAndRestart(e)
    }

    open fun onInvalidPin(e: AppException) {
        appSessionNavigator?.invalidateSessionAndRestart(e)
    }
}
