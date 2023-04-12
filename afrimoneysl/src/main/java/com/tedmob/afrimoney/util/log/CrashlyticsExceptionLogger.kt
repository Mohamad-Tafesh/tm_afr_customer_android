package com.tedmob.afrimoney.util.log

import com.tedmob.afrimoney.data.crashlytics.CrashlyticsHandler

class CrashlyticsExceptionLogger(private val crashlytics: CrashlyticsHandler) : ExceptionLogger {

    override fun saveLog(message: String) {
        crashlytics.log(message)
    }

    override fun logException(throwable: Throwable) {
        crashlytics.recordException(throwable)
    }
}