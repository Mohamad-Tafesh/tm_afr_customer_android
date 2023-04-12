package com.tedmob.afrimoney.app

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface ExecutionSchedulers {
    val subscribeScheduler: Scheduler
    val observeScheduler: Scheduler
}


object DefaultSchedulers : ExecutionSchedulers {
    override val observeScheduler: Scheduler = AndroidSchedulers.mainThread()
    override val subscribeScheduler: Scheduler = Schedulers.io()
}