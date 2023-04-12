package com.tedmob.afrimoney.app.usecase

abstract class SuspendableUseCase<T, in Params> {
    abstract suspend fun execute(params: Params): T
}