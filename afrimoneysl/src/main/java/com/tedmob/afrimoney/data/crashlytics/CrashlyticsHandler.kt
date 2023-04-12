package com.tedmob.afrimoney.data.crashlytics

import android.content.Context
import android.util.Log

interface CrashlyticsHandler {

    companion object {
        private const val TAG = "CrashlyticsHandler"

        private var platformInstance: CrashlyticsHandler? = null

        @Synchronized
        operator fun get(context: Context): CrashlyticsHandler {
            if (platformInstance != null) {
                return platformInstance!!
            }

            val google = context.attemptPlatformEquivalentFetch(
                "GMSCrashlyticsHandler",
                "gms",
                "Google"
            )
            if (google != null) {
                platformInstance = google
                return google
            }

            val huawei = context.attemptPlatformEquivalentFetch(
                "HMSCrashlyticsHandler",
                "hms",
                "Huawei"
            )
            if (huawei != null) {
                platformInstance = huawei
                return huawei
            }

            //others here

            val empty = context.attemptPlatformEquivalentFetch(
                "EmptyCrashlyticsHandler",
                "[empty]",
                "Empty"
            )
            if (empty != null) {
                platformInstance = empty
                return empty
            }

            throw IllegalStateException("CrashlyticsHandler could not find a proper platform to run the service on.")
        }

        fun reset() {
            platformInstance = null
        }


        private inline fun Context.attemptPlatformEquivalentFetch(
            containerClazzName: String,
            servicesName: String,
            platformLogTag: String
        ): CrashlyticsHandler? {
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

        private inline fun String.asPlatformAnalyticsHandlerClass(): Class<CrashlyticsHandler>? =
            try {
                Class.forName("${CrashlyticsHandler::class.java.`package`!!.name}.$this")
                    .let { it as Class<CrashlyticsHandler> }
            } catch (e: ClassNotFoundException) {
                null
            } catch (e: ClassCastException) {
                null
            }

        private inline fun Class<CrashlyticsHandler>.newInstanceIfAvailableReflection(context: Context): CrashlyticsHandler? =
            try {
                getMethod("newInstanceIfAvailable", Context::class.java)
                    .invoke(null, context)
                    .let { it as? CrashlyticsHandler? }
            } catch (e: NoSuchMethodException) {
                null
            }
    }


    fun setCrashCollectionEnabled(enable: Boolean)

    fun log(message: String)

    fun recordException(t: Throwable)

    fun setUserId(id: String)

    fun setCustomKey(key: String, value: String)
}