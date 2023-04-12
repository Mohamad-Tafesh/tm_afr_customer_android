package com.tedmob.afrimoney.data.analytics

import android.content.Context
import android.os.Bundle
import android.util.Log

interface AnalyticsHandler {

    companion object {
        private const val TAG = "AnalyticsHandler"

        private var platformInstance: AnalyticsHandler? = null

        @Synchronized
        operator fun get(context: Context): AnalyticsHandler {
            if (platformInstance != null) {
                return platformInstance!!
            }

            val google = context.attemptPlatformEquivalentFetch(
                "GMSAnalyticsHandler",
                "gms",
                "Google"
            )
            if (google != null) {
                platformInstance = google
                return google
            }

            val huawei = context.attemptPlatformEquivalentFetch(
                "HMSAnalyticsHandler",
                "hms",
                "Huawei"
            )
            if (huawei != null) {
                platformInstance = huawei
                return huawei
            }

            //others here

            val empty = context.attemptPlatformEquivalentFetch(
                "EmptyAnalyticsHandler",
                "[empty]",
                "Empty"
            )
            if (empty != null) {
                platformInstance = empty
                return empty
            }

            throw IllegalStateException("AnalyticsHandler could not find a proper platform to run the service on.")
        }

        fun reset() {
            platformInstance = null
        }


        private inline fun Context.attemptPlatformEquivalentFetch(
            containerClazzName: String,
            servicesName: String,
            platformLogTag: String
        ): AnalyticsHandler? {
            val containerClazz = containerClazzName.asPlatformAnalyticsHandlerClass()
            return if (containerClazz == null) {
                Log.d(
                    TAG,
                    "$platformLogTag failure: the related $servicesName dependencies are not available in your project."
                )
                null
            } else {
                val containerImpl = containerClazz.newInstanceIfAvailableReflection(this)
                if (containerImpl == null) {
                    Log.d(
                        TAG,
                        "$platformLogTag failure: the device does not seem to support $servicesName services."
                    )
                    null
                } else {
                    Log.d(
                        TAG,
                        "$platformLogTag success."
                    )
                    containerImpl
                }
            }
        }

        private inline fun String.asPlatformAnalyticsHandlerClass(): Class<AnalyticsHandler>? =
            try {
                Class.forName("${AnalyticsHandler::class.java.`package`!!.name}.$this")
                    .let { it as Class<AnalyticsHandler> }
            } catch (e: ClassNotFoundException) {
                null
            } catch (e: ClassCastException) {
                null
            }

        private inline fun Class<AnalyticsHandler>.newInstanceIfAvailableReflection(context: Context): AnalyticsHandler? =
            try {
                getMethod("newInstanceIfAvailable", Context::class.java)
                    .invoke(null, context)
                    .let { it as? AnalyticsHandler? }
            } catch (e: NoSuchMethodException) {
                null
            }
    }


    fun setAnalyticsEnabled(enable: Boolean)

    fun logScreenViewStart(screenName: String, screenClass: String)

    fun logScreenViewEnd(screenName: String)

    fun setUserProperty(key: String, name: String?)

    fun logCustomEvent(name: String, data: Bundle?)

    fun setUserId(id: String?)
}