package com.tedmob.afrimoney.util.location

import android.content.Context
import android.content.IntentSender
import android.location.Location
import android.util.Log
import androidx.annotation.RequiresPermission
import com.tedmob.afrimoney.exception.AppException

interface AppLocationServices {

    companion object {
        private const val TAG = "AppLocationServices"

        private var platformInstance: AppLocationServices? = null

        @Synchronized
        operator fun get(context: Context): AppLocationServices {
            if (platformInstance != null) {
                return platformInstance!!
            }

            val google = context.attemptPlatformEquivalentFetch(
                "GMSAppLocationServices",
                "gms",
                "Google"
            )
            if (google != null) {
                platformInstance = google
                return google
            }

            val huawei = context.attemptPlatformEquivalentFetch(
                "HMSAppLocationServices",
                "hms",
                "Huawei"
            )
            if (huawei != null) {
                platformInstance = huawei
                return huawei
            }

            //Amazon: NOOO!
            //others: NOOOOOOOOOOO!

            throw AppException("AppLocationServices could not find a proper platform to run the service on.")
        }

        private inline fun Context.attemptPlatformEquivalentFetch(
            containerClazzName: String,
            packageSuffix: String,
            platformLogTag: String
        ): AppLocationServices? {
            val containerClazz = containerClazzName.asAppLocationServicesClass(packageSuffix)
            return if (containerClazz == null) {
                Log.d(
                    TAG,
                    "$platformLogTag failure: the related $packageSuffix dependencies are not available in your project."
                )
                null
            } else {
                val containerImpl = containerClazz.newInstanceIfAvailableReflection(this)
                if (containerImpl == null) {
                    Log.d(
                        TAG,
                        "$platformLogTag failure: the device does not seem to support $packageSuffix services."
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


        private inline fun String.asAppLocationServicesClass(suffix: String): Class<AppLocationServices>? =
            try {
                Class.forName("${AppLocationServices::class.java.`package`!!.name}.$suffix.$this")
                    .let { it as Class<AppLocationServices> }
            } catch (e: ClassNotFoundException) {
                null
            } catch (e: ClassCastException) {
                null
            }

        private inline fun Class<AppLocationServices>.newInstanceIfAvailableReflection(context: Context): AppLocationServices? =
            try {
                getMethod("newInstanceIfAvailable", Context::class.java)
                    .invoke(null, context)
                    .let { it as? AppLocationServices? }
            } catch (e: NoSuchMethodException) {
                null
            }
    }


    fun getSettingsClient(): SettingsClient
    fun getFusedLocationProviderClient(): FusedLocationProviderClient


    interface SettingsClient {
        fun checkLocationSettings(): Task<Unit>
    }

    interface FusedLocationProviderClient {
        @RequiresPermission(anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"])
        fun getLastLocation(): Task<Location?>

        @RequiresPermission(anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"])
        fun requestLocationUpdates(
            request: AppLocationRequest,
            onLocationObtained: (location: Location) -> Unit,
        ): Task<Unit>

        fun removeLocationUpdates(
            request: AppLocationRequest,
        ): Task<Unit>
    }


    class Task<T> {
        var onComplete: (() -> Unit)? = null
            private set
        var onSuccess: ((T) -> Unit)? = null
            private set
        var onFailure: ((e: ApiException?) -> Unit)? = null
            private set


        fun addOnComplete(onComplete: () -> Unit) = apply {
            this.onComplete = onComplete
        }

        fun addOnSuccess(onSuccess: (T) -> Unit) = apply {
            this.onSuccess = onSuccess
        }

        fun addOnFailure(onFailure: (e: ApiException?) -> Unit) = apply {
            this.onFailure = onFailure
        }
    }

    sealed class ApiException(delegate: Exception) : Exception(delegate)
    class SettingsUnavailableException(delegate: Exception) : ApiException(delegate)
    class ResolvableApiException(delegate: Exception, val intentSender: IntentSender) : ApiException(delegate)
    class OtherApiException(delegate: Exception) : ApiException(delegate)
}