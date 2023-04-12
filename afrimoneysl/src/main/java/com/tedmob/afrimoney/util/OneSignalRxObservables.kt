package com.tedmob.afrimoney.util

import com.onesignal.OSSubscriptionObserver
import com.onesignal.OSSubscriptionStateChanges
import com.onesignal.OneSignal
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers

inline val oneSignalUserIdSingle: Single<String>
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

inline val oneSignalInfoSingle: Single<OneSignalInfo>
    get() = Single.create<OneSignalInfo> {
        val observer = object : OSSubscriptionObserver {
            override fun onOSSubscriptionChanged(stateChanges: OSSubscriptionStateChanges?) {
                it.onSuccess(
                    OneSignalInfo(
                        stateChanges?.to?.userId,
                    )
                )
                OneSignal.removeSubscriptionObserver(this)
            }
        }
        OneSignal.addSubscriptionObserver(observer)
    }
        .subscribeOn(AndroidSchedulers.mainThread())

inline fun <reified T : Any> getOneSignalIdsTransformer(): SingleTransformer<T, ContainingOneSignalInfo<T>> =
    SingleTransformer<T, ContainingOneSignalInfo<T>> { source ->
        source.flatMap { data ->
            oneSignalInfoSingle.map {
                ContainingOneSignalInfo(data, it)
            }
        }
    }


class OneSignalInfo(
    val userId: String?,
)

class ContainingOneSignalInfo<T : Any>(val data: T, val info: OneSignalInfo)