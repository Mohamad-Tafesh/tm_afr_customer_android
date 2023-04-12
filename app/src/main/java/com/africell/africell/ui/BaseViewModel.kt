package com.africell.africell.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.africell.africell.app.AppSessionNavigator
import com.africell.africell.app.handleInvalidSession
import com.tedmob.afrimoney.data.Resource
import com.africell.africell.exception.AppException
import com.africell.africell.exception.AppExceptionFactory
import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.emitError
import com.tedmob.afrimoney.data.emitLoading
import com.tedmob.afrimoney.data.emitSuccess
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


abstract class BaseViewModel : ViewModel() {

    protected val jobsMap = mutableMapOf<Any, Job>()


    protected inline fun <reified T, reified Params> execute(
        useCase: SuspendableUseCase<T, Params>,
        params: Params,
        crossinline onLoading: suspend () -> Unit = {},
        crossinline onSuccess: suspend (data: T) -> Unit = {},
        crossinline onError: suspend (e: Throwable) -> Unit = {}
    ): Job {
        return viewModelScope.launch {
            kotlin.runCatching {
                onLoading()
                onSuccess(useCase.execute(params))
            }.onFailure {
                if (it is CancellationException) throw it
                onError(it)
            }
        }
    }

    protected inline fun <reified T, reified Params> execute(
        key: Any,
        useCase: SuspendableUseCase<T, Params>,
        params: Params,
        crossinline onLoading: suspend () -> Unit = {},
        crossinline onSuccess: suspend (data: T) -> Unit = {},
        crossinline onError: suspend (e: Throwable) -> Unit = {}
    ): Job {
        jobsMap[key] = execute(useCase, params, onLoading, onSuccess, onError)
        return jobsMap[key]!!
    }

    protected inline fun <reified T, reified Params> executeResource(
        useCase: SuspendableUseCase<T, Params>,
        params: Params,
        liveData: MutableLiveData<Resource<T>>,
        appExceptionFactory: AppExceptionFactory,
        appSessionNavigator: AppSessionNavigator? = null,
        noinline action: (() -> Unit)? = null
    ): Job {
        return execute(
            useCase,
            params,
            onLoading = {
                liveData.emitLoading()
            },
            onSuccess = {
                liveData.emitSuccess(it)
            },
            onError = {
                appExceptionFactory.make(it)
                    .let { exception ->
                        if (appSessionNavigator != null) {
                            exception.handleInvalidSession(appSessionNavigator) {
                                liveData.emitError(it.userMessage, action)
                            }
                        } else {
                            liveData.emitError(exception.userMessage, action)
                        }
                    }
            }
        )
    }

    protected inline fun <reified T, reified Params> executeResource(
        key: Any,
        useCase: SuspendableUseCase<T, Params>,
        params: Params,
        liveData: MutableLiveData<Resource<T>>,
        appExceptionFactory: AppExceptionFactory,
        appSessionNavigator: AppSessionNavigator? = null,
        noinline action: (() -> Unit)? = null
    ): Job {
        jobsMap[key] = executeResource(useCase, params, liveData, appExceptionFactory, appSessionNavigator, action)
        return jobsMap[key]!!
    }


    protected inline fun addJob(jobKey: Any, noinline block: suspend CoroutineScope.() -> Unit) {
        jobsMap[jobKey] = viewModelScope.launch(block = block)
    }

    protected inline fun cancel(jobKey: Any) {
        jobsMap[jobKey]?.cancel()
    }

    @Suppress("unused")
    protected inline fun <T> CoroutineScope.runCoroutineCatching(block: () -> T): Result<T> {
        return try {
            Result.success(block())
        } catch (e: Throwable) {
            if (e is CancellationException) throw e
            Result.failure(e)
        }
    }

    protected inline fun <T> Result<T>.successOrCancel(): T =
        try {
            getOrThrow()
        } catch (_: Exception) {
            throw CancellationException()
        }

    protected inline fun <T> Result<T>.onAppFailure(
        appExceptionFactory: AppExceptionFactory,
        appSessionNavigator: AppSessionNavigator? = null,
        action: (exception: AppException) -> Unit
    ): Result<T> {
        exceptionOrNull()?.let {
            val exception = appExceptionFactory.make(it)
            if (appSessionNavigator != null) {
                exception.handleInvalidSession(appSessionNavigator) {
                    action(it)
                }
            } else {
                action(exception)
            }
        }
        return this
    }
}