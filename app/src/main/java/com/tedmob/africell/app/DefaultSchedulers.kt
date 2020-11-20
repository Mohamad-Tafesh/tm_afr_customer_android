package com.tedmob.africell.app

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DefaultSchedulers : ExecutionSchedulers {
    override val observeScheduler: Scheduler = AndroidSchedulers.mainThread()

    override val subscribeScheduler: Scheduler = Schedulers.io()
}