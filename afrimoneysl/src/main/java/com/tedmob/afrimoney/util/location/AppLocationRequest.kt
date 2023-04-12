package com.tedmob.afrimoney.util.location

import android.os.SystemClock

class AppLocationRequest {

    sealed interface Priority {
        object HighAccuracy : Priority
        object BalancedPowerAccuracy : Priority
        object LowPower : Priority
        object NoPower : Priority
    }


    var priority: Priority = Priority.BalancedPowerAccuracy
    var interval: Long = 3_600_000L
        set(value) {
            field = value.coerceAtLeast(0L)
        }
    var fastestInterval: Long = 600_000L
        set(value) {
            field = value.coerceAtLeast(0L)
        }
    var expirationTime: Long = Long.MAX_VALUE
        set(value) {
            field = value.coerceAtLeast(0L)
        }

    inline fun setExpirationDuration(duration: Long) {
        val elapsedTime = SystemClock.elapsedRealtime()
        expirationTime = if (duration > Long.MAX_VALUE - elapsedTime) Long.MAX_VALUE else (elapsedTime + duration)
    }

    var numUpdates: Int = Int.MAX_VALUE
        set(value) {
            field = value.coerceAtLeast(0)
        }
    var smallestDisplacement: Float = 0f
        set(value) {
            field = value.coerceAtLeast(0f)
        }
    var maxWaitTime: Long = 0L
        set(value) {
            field = value.coerceAtLeast(0L)
        }
}

@DslMarker
annotation class AppLocationRequestDslMarker


@AppLocationRequestDslMarker
class AppLocationRequestBuilder {
    var priority: AppLocationRequest.Priority = AppLocationRequest.Priority.BalancedPowerAccuracy
    var interval: Long = 3_600_000L
    var fastestInterval: Long = 600_000L
    var expirationTime: Long = Long.MAX_VALUE

    inline fun setExpirationDuration(duration: Long) {
        val elapsedTime = SystemClock.elapsedRealtime()
        expirationTime = if (duration > Long.MAX_VALUE - elapsedTime) Long.MAX_VALUE else (elapsedTime + duration)
    }

    var numUpdates: Int = Int.MAX_VALUE
    var smallestDisplacement: Float = 0f
    var maxWaitTime: Long = 0L

    fun build(): AppLocationRequest = AppLocationRequest()
        .also {
            it.priority = priority
            it.interval = interval
            it.fastestInterval = fastestInterval
            it.expirationTime = expirationTime
            it.numUpdates = numUpdates
            it.smallestDisplacement = smallestDisplacement
            it.maxWaitTime = maxWaitTime
        }
}

inline fun appLocationRequest(block: AppLocationRequestBuilder.() -> Unit): AppLocationRequest =
    AppLocationRequestBuilder().apply(block).build()