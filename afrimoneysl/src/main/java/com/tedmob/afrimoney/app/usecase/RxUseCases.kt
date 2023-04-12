package com.tedmob.afrimoney.app.usecase

import com.tedmob.afrimoney.app.DefaultSchedulers
import com.tedmob.afrimoney.app.ExecutionSchedulers
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver


abstract class RxUseCase<T, in Params, Source, in Observer : Disposable> protected constructor(
    protected val schedulers: ExecutionSchedulers = DefaultSchedulers
) {
    protected val disposables: CompositeDisposable = CompositeDisposable()


    abstract fun buildUseCaseObservable(params: Params): Source

    abstract fun execute(params: Params, observer: Observer)


    fun dispose() {
        if (!disposables.isDisposed) {
            disposables.dispose()
        }
    }

    fun clear() {
        disposables.clear()
    }
}

abstract class ObservableUseCase<T, in Params> protected constructor(
    schedulers: ExecutionSchedulers = DefaultSchedulers
) : RxUseCase<T, Params, Observable<T>, DisposableObserver<T>>(schedulers) {

    override fun execute(params: Params, observer: DisposableObserver<T>) {
        val observable = this.buildUseCaseObservable(params)
            .subscribeOn(schedulers.subscribeScheduler)
            .observeOn(schedulers.observeScheduler)
        disposables.add(observable.subscribeWith(observer))
    }
}

abstract class SingleUseCase<T, in Params> protected constructor(
    schedulers: ExecutionSchedulers = DefaultSchedulers
) : RxUseCase<T, Params, Single<T>, DisposableSingleObserver<T>>(schedulers) {

    override fun execute(params: Params, observer: DisposableSingleObserver<T>) {
        val observable = this.buildUseCaseObservable(params)
            .subscribeOn(schedulers.subscribeScheduler)
            .observeOn(schedulers.observeScheduler)
        disposables.add(observable.subscribeWith(observer))
    }
}