package com.africell.africell.util.log

import com.google.firebase.crashlytics.FirebaseCrashlytics

class CrashlyticsExceptionLogger(private val firebaseCrashlytics: FirebaseCrashlytics) : ExceptionLogger {

    override fun saveLog(message: String) {
        firebaseCrashlytics.log(message)
    }

    override fun logException(throwable: Throwable) {
        firebaseCrashlytics.recordException(throwable)
    }
}