package com.tedmob.afrimoney.app

import android.util.Log
import com.tedmob.afrimoney.data.crashlytics.CrashlyticsHandler
import timber.log.Timber

class CrashlyticsTree(private val crashlytics: CrashlyticsHandler) : Timber.Tree() {
    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return priority >= Log.ERROR
    }

    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
        val priorityStr = when (priority) {
            Log.ASSERT -> "A/"
            Log.DEBUG -> "D/"
            Log.ERROR -> "E/"
            Log.INFO -> "I/"
            Log.VERBOSE -> "V/"
            Log.WARN -> "W/"
            else -> "V/"
        }
        crashlytics.log("$priorityStr$tag: $message")
        throwable?.let { crashlytics.recordException(it) }
    }
}