package com.tedmob.africell.util

import com.onesignal.OneSignal
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

inline val oneSignalIdsObservable
    get() = Observable.create<Pair<String, String>> {
        OneSignal.idsAvailable { userId, registrationId ->
            it.onNext(userId to registrationId)
            it.onComplete()
        }
    }
        .subscribeOn(AndroidSchedulers.mainThread())