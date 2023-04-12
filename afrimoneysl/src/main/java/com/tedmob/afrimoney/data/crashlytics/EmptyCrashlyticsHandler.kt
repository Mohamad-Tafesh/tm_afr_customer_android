package com.tedmob.afrimoney.data.crashlytics

import android.content.Context

@Suppress("unused")
class EmptyCrashlyticsHandler : CrashlyticsHandler {

    companion object {
        @Suppress("UNUSED_PARAMETER")
        @JvmStatic
        fun newInstanceIfAvailable(context: Context): CrashlyticsHandler? = EmptyCrashlyticsHandler()
    }


    override fun setCrashCollectionEnabled(enable: Boolean) {
    }

    override fun log(message: String) {
    }

    override fun recordException(t: Throwable) {
    }

    override fun setUserId(id: String) {
    }

    override fun setCustomKey(key: String, value: String) {
    }
}