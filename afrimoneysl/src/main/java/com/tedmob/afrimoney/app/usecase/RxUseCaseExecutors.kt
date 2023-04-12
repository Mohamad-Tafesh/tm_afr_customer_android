package com.tedmob.afrimoney.app.usecase

import io.reactivex.disposables.Disposable

abstract class RxUseCaseExecutor<T: Any, in Params, Observer : Disposable, UseCase : RxUseCase<T, Params, *, Observer>>(
    protected val useCase: UseCase,
    private val params: Params
) {
    fun execute() {
        preExecute()
        useCase.execute(params, getObserver())
    }

    open fun preExecute() {
    }

    abstract fun getObserver(): Observer
}

abstract class ObservableUseCaseExecutor<T: Any, Params>(
    useCase: ObservableUseCase<T, Params>,
    params: Params
) : RxUseCaseExecutor<T, Params, DefaultObserver<T>, ObservableUseCase<T, Params>>(useCase, params)

abstract class SingleUseCaseExecutor2<T: Any, Params>(
    useCase: SingleUseCase<T, Params>,
    params: Params
) : RxUseCaseExecutor<T, Params, DefaultSingleObserver<T>, SingleUseCase<T, Params>>(useCase, params)

//...


abstract class UseCaseExecutor<T: Any, in Params>(
    private val useCase: ObservableUseCase<T, Params>,
    private val params: Params
) {
    fun execute() {
        preExecute()
        useCase.execute(params, getObserver())
    }

    open fun preExecute() {
    }

    abstract fun getObserver(): DefaultObserver<T>
}

abstract class SingleUseCaseExecutor<T: Any, in Params>(
    private val useCase: SingleUseCase<T, Params>,
    private val params: Params
) {
    fun execute() {
        preExecute()
        useCase.execute(params, getObserver())
    }

    open fun preExecute() {
    }

    abstract fun getObserver(): DefaultSingleObserver<T>
}