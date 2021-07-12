package com.africell.africell.app

abstract class UseCaseExecutor<T, in Params>(
    private val useCase: UseCase<T, Params>,
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