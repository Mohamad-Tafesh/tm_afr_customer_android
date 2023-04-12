package com.tedmob.afrimoney.data.analytics

import android.content.Context
import android.os.Bundle
/*import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.api.ConnectionResult
import com.huawei.hms.api.HuaweiApiAvailability*/

@Suppress("unused")
class HMSAnalyticsHandler(context: Context) : AnalyticsHandler {

    companion object {
        @JvmStatic
        fun newInstanceIfAvailable(context: Context): AnalyticsHandler? =
            /*HuaweiApiAvailability.getInstance().isHuaweiMobileServicesAvailable(context)
                .let {
                    when (it) {
                        ConnectionResult.SUCCESS -> HMSAnalyticsHandler(context)
                        ConnectionResult.SERVICE_MISSING -> null
                        ConnectionResult.SERVICE_UPDATING -> HMSAnalyticsHandler(context)
                        ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED -> HMSAnalyticsHandler(context)
                        ConnectionResult.SERVICE_DISABLED -> null
                        ConnectionResult.SERVICE_INVALID -> null
                        else -> null
                    }
                }*/
            null
    }


    /*@SuppressLint("MissingPermission")
    private val instance = HiAnalytics.getInstance(context)*/


    override fun setAnalyticsEnabled(enable: Boolean) {
        //instance.setAnalyticsEnabled(enable)
    }

    override fun logScreenViewStart(screenName: String, screenClass: String) {
        //instance.pageStart(screenName, screenClass)
    }

    override fun logScreenViewEnd(screenName: String) {
        //instance.pageEnd(screenName)
    }

    override fun setUserProperty(key: String, name: String?) {
        //instance.setUserProfile(key, name)
    }

    override fun logCustomEvent(name: String, data: Bundle?) {
        //instance.onEvent(name, data)
    }

    override fun setUserId(id: String?) {
        //instance.setUserId(id)
    }
}