package com.africell.africell.app

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class CrashlyticsTree(private val firebaseCrashlytics: FirebaseCrashlytics) : Timber.Tree() {
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
        firebaseCrashlytics.log("$priorityStr$tag: $message")
        throwable?.let { firebaseCrashlytics.recordException(it) }
    }
}