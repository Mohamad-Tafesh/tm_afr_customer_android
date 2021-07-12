package com.africell.maronite.util

import com.onesignal.OSSubscriptionObserver
import com.onesignal.OSSubscriptionStateChanges
import com.onesignal.OneSignal
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

inline val oneSignalIdsObservable
    get() = Observable.create<String> {
        val userId = OneSignal.getDeviceState()?.userId
        if (!userId.isNullOrEmpty()) {
            //shortcut
            it.onNext(userId)
            it.onComplete()
        } else {
            val observer = object : OSSubscriptionObserver {
                override fun onOSSubscriptionChanged(stateChanges: OSSubscriptionStateChanges?) {
                    it.onNext(stateChanges?.to?.userId.orEmpty())
                    OneSignal.removeSubscriptionObserver(this)
                    it.onComplete()
                }
            }
            OneSignal.addSubscriptionObserver(observer)
        }
    }
        .subscribeOn(AndroidSchedulers.mainThread())

inline val oneSignalIdsSingle: Single<String>
    get() = Single.create<String> {
        val userId = OneSignal.getDeviceState()?.userId
        if (!userId.isNullOrEmpty()) {
            //shortcut
            it.onSuccess(userId)
        } else {
            val observer = object : OSSubscriptionObserver {
                override fun onOSSubscriptionChanged(stateChanges: OSSubscriptionStateChanges?) {
                    it.onSuccess(stateChanges?.to?.userId.orEmpty())
                    OneSignal.removeSubscriptionObserver(this)
                }
            }
            OneSignal.addSubscriptionObserver(observer)
        }
    }
        .subscribeOn(AndroidSchedulers.mainThread())