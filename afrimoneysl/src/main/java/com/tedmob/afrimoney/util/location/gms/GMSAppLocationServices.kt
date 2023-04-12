package com.tedmob.afrimoney.util.location.gms

import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.annotation.RequiresPermission
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.tedmob.afrimoney.util.location.AppLocationRequest
import com.tedmob.afrimoney.util.location.AppLocationServices

class GMSAppLocationServices(private val context: Context) : AppLocationServices {

    companion object {
        @JvmStatic
        fun newInstanceIfAvailable(context: Context): AppLocationServices? =
            GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context)
                .let {
                    when (it) {
                        ConnectionResult.SUCCESS -> GMSAppLocationServices(context)
                        ConnectionResult.SERVICE_MISSING -> null
                        ConnectionResult.SERVICE_UPDATING -> GMSAppLocationServices(context)
                        ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED -> GMSAppLocationServices(context)
                        ConnectionResult.SERVICE_DISABLED -> null
                        ConnectionResult.SERVICE_INVALID -> null
                        else -> null
                    }
                }
    }


    override fun getSettingsClient(): AppLocationServices.SettingsClient =
        SettingsClient(LocationServices.getSettingsClient(context))

    class SettingsClient(
        private val delegate: com.google.android.gms.location.SettingsClient
    ) : AppLocationServices.SettingsClient {

        override fun checkLocationSettings(): AppLocationServices.Task<Unit> {
            val task = AppLocationServices.Task<Unit>()

            delegate.checkLocationSettings(
                LocationSettingsRequest.Builder()
                    .addLocationRequest(
                        LocationRequest.create()
                            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    )
                    .build()
            )
                .addOnSuccessListener { task.onSuccess?.invoke(Unit) }
                .addOnFailureListener {
                    val exception = (it as? ApiException)?.let {
                        when (it.statusCode) {
                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                                if (it is ResolvableApiException) {
                                    AppLocationServices.ResolvableApiException(it, it.resolution.intentSender)
                                } else {
                                    AppLocationServices.OtherApiException(it)
                                }

                            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE ->
                                AppLocationServices.SettingsUnavailableException(it)

                            else -> AppLocationServices.OtherApiException(it)
                        }
                    }

                    task.onFailure?.invoke(exception)
                }
                .addOnCompleteListener { task.onComplete?.invoke() }

            return task
        }
    }


    override fun getFusedLocationProviderClient(): AppLocationServices.FusedLocationProviderClient =
        fusedLocationProviderClient

    private val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        FusedLocationProviderClient(LocationServices.getFusedLocationProviderClient(context))
    }


    class FusedLocationProviderClient(
        private val delegate: com.google.android.gms.location.FusedLocationProviderClient
    ) : AppLocationServices.FusedLocationProviderClient {

        private var lastLocationCallback: LocationCallback? = null


        @RequiresPermission(anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"])
        override fun getLastLocation(): AppLocationServices.Task<Location?> {
            val task = AppLocationServices.Task<Location?>()

            delegate.lastLocation
                .addOnSuccessListener { task.onSuccess?.invoke(it) }
                .addOnFailureListener {
                    val exception = (it as? ApiException)?.let {
                        when (it.statusCode) {
                            CommonStatusCodes.RESOLUTION_REQUIRED ->
                                if (it is ResolvableApiException) {
                                    AppLocationServices.ResolvableApiException(it, it.resolution.intentSender)
                                } else {
                                    AppLocationServices.OtherApiException(it)
                                }

                            else -> AppLocationServices.OtherApiException(it)
                        }
                    }

                    task.onFailure?.invoke(exception)
                }
                .addOnCompleteListener { task.onComplete?.invoke() }

            return task
        }

        @RequiresPermission(anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"])
        override fun requestLocationUpdates(
            request: AppLocationRequest,
            onLocationObtained: (location: Location) -> Unit,
        ): AppLocationServices.Task<Unit> {
            val task = AppLocationServices.Task<Unit>()

            lastLocationCallback = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    p0.lastLocation?.let { onLocationObtained(it) }
                }
            }

            delegate.requestLocationUpdates(
                request.toGMSLocationRequest(),
                lastLocationCallback!!,
                Looper.getMainLooper()
            )
                .addOnSuccessListener { task.onSuccess?.invoke(Unit) }
                .addOnFailureListener {
                    val exception = (it as? ApiException)?.let {
                        when (it.statusCode) {
                            CommonStatusCodes.RESOLUTION_REQUIRED ->
                                if (it is ResolvableApiException) {
                                    AppLocationServices.ResolvableApiException(it, it.resolution.intentSender)
                                } else {
                                    AppLocationServices.OtherApiException(it)
                                }

                            else -> AppLocationServices.OtherApiException(it)
                        }
                    }

                    task.onFailure?.invoke(exception)
                }
                .addOnCompleteListener { task.onComplete?.invoke() }

            return task
        }

        override fun removeLocationUpdates(request: AppLocationRequest): AppLocationServices.Task<Unit> {
            val task = AppLocationServices.Task<Unit>()

            lastLocationCallback?.let {
                delegate.removeLocationUpdates(it)
                    .addOnSuccessListener { task.onSuccess?.invoke(Unit) }
                    .addOnFailureListener {
                        val exception = (it as? ApiException)?.let {
                            when (it.statusCode) {
                                CommonStatusCodes.RESOLUTION_REQUIRED ->
                                    if (it is ResolvableApiException) {
                                        AppLocationServices.ResolvableApiException(it, it.resolution.intentSender)
                                    } else {
                                        AppLocationServices.OtherApiException(it)
                                    }

                                else -> AppLocationServices.OtherApiException(it)
                            }
                        }

                        task.onFailure?.invoke(exception)
                    }
                    .addOnCompleteListener { task.onComplete?.invoke() }
            } ?: run {
                task.onSuccess?.invoke(Unit)
                task.onComplete?.invoke()
            }

            return task
        }
    }
}