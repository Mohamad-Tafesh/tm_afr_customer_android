package com.tedmob.afrimoney.data.crashlytics

import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.crashlytics.FirebaseCrashlytics

@Suppress("unused")
class GMSCrashlyticsHandler : CrashlyticsHandler {

    companion object {
        @JvmStatic
        fun newInstanceIfAvailable(context: Context): CrashlyticsHandler? =
            GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context)
                .let {
                    when (it) {
                        ConnectionResult.SUCCESS -> GMSCrashlyticsHandler()
                        ConnectionResult.SERVICE_MISSING -> null
                        ConnectionResult.SERVICE_UPDATING -> GMSCrashlyticsHandler()
                        ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED -> GMSCrashlyticsHandler()
                        ConnectionResult.SERVICE_DISABLED -> null
                        ConnectionResult.SERVICE_INVALID -> null
                        else -> null
                    }
                }
    }


    private val instance = FirebaseCrashlytics.getInstance()


    override fun setCrashCollectionEnabled(enable: Boolean) {
        instance.setCrashlyticsCollectionEnabled(enable)
    }

    override fun log(message: String) {
        instance.log(message)
    }

    override fun recordException(t: Throwable) {
        instance.recordException(t)
    }

    override fun setUserId(id: String) {
        instance.setUserId(id)
    }

    override fun setCustomKey(key: String, value: String) {
        instance.setCustomKey(key, value)
    }
}