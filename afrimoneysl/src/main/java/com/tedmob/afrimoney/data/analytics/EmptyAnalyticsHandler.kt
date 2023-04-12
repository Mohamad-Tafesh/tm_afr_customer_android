package com.tedmob.afrimoney.data.analytics

import android.content.Context
import android.os.Bundle

@Suppress("unused")
class EmptyAnalyticsHandler : AnalyticsHandler {

    companion object {
        @Suppress("UNUSED_PARAMETER")
        @JvmStatic
        fun newInstanceIfAvailable(context: Context): AnalyticsHandler? = EmptyAnalyticsHandler()
    }


    override fun setAnalyticsEnabled(enable: Boolean) {
    }

    override fun logScreenViewStart(screenName: String, screenClass: String) {
    }

    override fun logScreenViewEnd(screenName: String) {
    }

    override fun setUserProperty(key: String, name: String?) {
    }

    override fun logCustomEvent(name: String, data: Bundle?) {
    }

    override fun setUserId(id: String?) {
    }
}