package com.tedmob.afrimoney.data.crashlytics

import android.content.Context

/*import com.huawei.agconnect.crash.AGConnectCrash
import com.huawei.hms.api.ConnectionResult
import com.huawei.hms.api.HuaweiApiAvailability
import com.tedmob.afrimoney.data.crashlytics.internal.PlatformCrashlyticsHandler*/

@Suppress("unused")
internal class HMSCrashlyticsHandler : CrashlyticsHandler {

    companion object {
        @JvmStatic
        fun newInstanceIfAvailable(context: Context): CrashlyticsHandler? =
            /*HuaweiApiAvailability.getInstance().isHuaweiMobileServicesAvailable(context)
                .let {
                    when (it) {
                        ConnectionResult.SUCCESS -> HMSCrashlyticsHandler()
                        ConnectionResult.SERVICE_MISSING -> null
                        ConnectionResult.SERVICE_UPDATING -> HMSCrashlyticsHandler()
                        ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED -> HMSCrashlyticsHandler()
                        ConnectionResult.SERVICE_DISABLED -> null
                        ConnectionResult.SERVICE_INVALID -> null
                        else -> null
                    }
                }*/
            null
    }


    //private val instance by lazy { AGConnectCrash.getInstance() }


    override fun setCrashCollectionEnabled(enable: Boolean) {
        //instance.enableCrashCollection(enable)
    }

    override fun log(message: String) {
        //instance.log(message)
    }

    override fun recordException(t: Throwable) {
        //instance.recordException(t)
    }

    override fun setUserId(id: String) {
        //instance.setUserId(id)
    }

    override fun setCustomKey(key: String, value: String) {
        //instance.setCustomKey(key, value)
    }
}