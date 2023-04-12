package com.tedmob.afrimoney.data.analytics

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.analytics.FirebaseAnalytics

@Suppress("unused")
class GMSAnalyticsHandler(context: Context) : AnalyticsHandler {

    companion object {
        @JvmStatic
        fun newInstanceIfAvailable(context: Context): AnalyticsHandler? =
            GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context)
                .let {
                    when (it) {
                        ConnectionResult.SUCCESS -> GMSAnalyticsHandler(context)
                        ConnectionResult.SERVICE_MISSING -> null
                        ConnectionResult.SERVICE_UPDATING -> GMSAnalyticsHandler(context)
                        ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED -> GMSAnalyticsHandler(context)
                        ConnectionResult.SERVICE_DISABLED -> null
                        ConnectionResult.SERVICE_INVALID -> null
                        else -> null
                    }
                }
    }


    @SuppressLint("MissingPermission")
    private val instance = FirebaseAnalytics.getInstance(context)


    override fun setAnalyticsEnabled(enable: Boolean) {
        instance.setAnalyticsCollectionEnabled(enable)
    }

    override fun logScreenViewStart(screenName: String, screenClass: String) {
        instance.logEvent(
            FirebaseAnalytics.Event.SCREEN_VIEW,
            bundleOf(
                FirebaseAnalytics.Param.SCREEN_NAME to screenName,
                FirebaseAnalytics.Param.SCREEN_CLASS to screenClass
            )
        )
    }

    override fun logScreenViewEnd(screenName: String) {
    }

    override fun setUserProperty(key: String, name: String?) {
        instance.setUserProperty(key, name)
    }

    override fun logCustomEvent(name: String, data: Bundle?) {
        instance.logEvent(name, data)
    }

    override fun setUserId(id: String?) {
        instance.setUserId(id)
    }
}