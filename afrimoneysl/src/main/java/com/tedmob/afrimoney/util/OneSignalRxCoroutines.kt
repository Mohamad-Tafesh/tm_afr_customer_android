package com.tedmob.afrimoney.util

import com.onesignal.OSSubscriptionObserver
import com.onesignal.OSSubscriptionStateChanges
import com.onesignal.OneSignal
import com.tedmob.afrimoney.exception.AppException
import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

//fixme have to do this because "addSubscriptionObserver" holds a weak reference to the observer, and it will be removed unless this exists
private var currentOneSignalObserver: OSSubscriptionObserver? = null

suspend fun suspendForOneSignalUserId(timeoutDuration: Duration = 1.minutes): String =
    withContext(Dispatchers.Default) {
        val userId = OneSignal.getDeviceState()?.userId

        if (!userId.isNullOrEmpty()) {
            userId
        } else {
            try {
                withTimeout(timeoutDuration) {
                    suspendCancellableCoroutine {
                        currentOneSignalObserver = object : OSSubscriptionObserver {
                            override fun onOSSubscriptionChanged(stateChanges: OSSubscriptionStateChanges?) {
                                if (stateChanges?.to?.userId != null) {
                                    it.resume(stateChanges.to?.userId.orEmpty())

                                    OneSignal.removeSubscriptionObserver(this)
                                    currentOneSignalObserver = null
                                }
                            }
                        }
                        it.invokeOnCancellation {
                            OneSignal.removeSubscriptionObserver(currentOneSignalObserver)
                            currentOneSignalObserver = null
                        }
                        OneSignal.addSubscriptionObserver(currentOneSignalObserver)
                    }
                }
            } catch (e: TimeoutCancellationException) {
                throw AppException("Timeout. Please retry again.")
            }
        }
    }

suspend fun suspendForOneSignalUserIdOrNull(timeoutDuration: Duration = 1.minutes): String? =
    withContext(Dispatchers.Default) {
        val userId = OneSignal.getDeviceState()?.userId

        if (!userId.isNullOrEmpty()) {
            userId
        } else {
            withTimeoutOrNull(timeoutDuration) {
                suspendCancellableCoroutine {
                    currentOneSignalObserver = object : OSSubscriptionObserver {
                        override fun onOSSubscriptionChanged(stateChanges: OSSubscriptionStateChanges?) {
                            if (stateChanges?.to?.userId != null) {
                                it.resume(stateChanges.to?.userId.orEmpty())

                                OneSignal.removeSubscriptionObserver(this)
                                currentOneSignalObserver = null
                            }
                        }
                    }
                    it.invokeOnCancellation {
                        OneSignal.removeSubscriptionObserver(currentOneSignalObserver)
                        currentOneSignalObserver = null
                    }
                    OneSignal.addSubscriptionObserver(currentOneSignalObserver)
                }
            }
        }
    }