package com.tedmob.foras.util.location

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.tedmob.afrimoney.R
import timber.log.Timber


abstract class LocationHelper @JvmOverloads constructor(
    private val context: Context,
    locationRequest: LocationRequest? = null,
    var callback: Callback? = null
) : OnCompleteListener<LocationSettingsResponse> {

    companion object {
        private const val LOCATION_UPDATE_INTERVAL = 5000L
        private const val LOCATION_UPDATE_INTERVAL_FAST = 2000L
    }

    object State {
        const val STOPPED = 0
        const val STARTED = 1
        const val REQ_PERMISSION = 2
        const val REQ_LOCATION_SETTINGS = 3
        const val READY = 4
        const val UPDATING = 5
    }

    private lateinit var locationSettingsRequest: LocationSettingsRequest

    private var actualLocationRequest = locationRequest
        ?: LocationRequest.create()
            .setInterval(LOCATION_UPDATE_INTERVAL)
            .setFastestInterval(LOCATION_UPDATE_INTERVAL_FAST)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

    protected var state = State.STOPPED
    var paused = false

    var permissionsRequestCode = 1337
    var locationSettingsRequestCode = 1338

    private var locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            p0.locations.lastOrNull()?.let { callback?.onLocationChanged(it) }
        }
    }


    fun setLocationRequest(locationRequest: LocationRequest) {
        actualLocationRequest = locationRequest
    }

    protected abstract fun onPermissionRequired()

    abstract fun requestLocationSettings(resolvableApiException: ResolvableApiException)

    fun init() {
        locationSettingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(actualLocationRequest)
            .setAlwaysShow(true)
            .build()
    }

    fun start() {
        state = State.STARTED
        init()

        if (state > State.STOPPED) {
            checkLocationPermission()
        }
    }

    fun stop() {
        stopLocationUpdates()
        state = State.STOPPED
    }

    private fun checkLocationPermission() {
        state = State.REQ_PERMISSION
        if (ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // permission required
            onPermissionRequired()
        } else {
            // we have the needed permission
            onPermissionGranted()
        }
    }

    fun onPermissionRequired(activity: Activity) {
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, ACCESS_FINE_LOCATION)) {
            showPermissionsExplanation(activity)
        } else {
            // No explanation needed, we can request the permission.
            requestPermission(activity)
        }
    }

    fun onPermissionRequired(fragment: Fragment) {
        // Should we show an explanation?
        if (fragment.shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
            showPermissionsExplanation(fragment)
        } else {
            // No explanation needed, we can request the permission.
            requestPermission(fragment)
        }
    }

    private fun showPermissionsExplanation(activity: Activity) {
        AlertDialog.Builder(activity)
            .setTitle(R.string.location_helper__permissions)
            .setMessage(R.string.location_helper__location_permission_explanation)
            .setNegativeButton(R.string.location_helper__cancel) { _, _ -> callback?.onPermissionDenied() }
            .setPositiveButton(R.string.location_helper__grant_permission) { _, _ -> requestPermission(activity) }
            .show()
    }

    private fun showPermissionsExplanation(fragment: Fragment) {
        AlertDialog.Builder(context)
            .setTitle(R.string.location_helper__permissions)
            .setMessage(R.string.location_helper__location_permission_explanation)
            .setNegativeButton(R.string.location_helper__cancel) { _, _ -> callback?.onPermissionDenied() }
            .setPositiveButton(R.string.location_helper__grant_permission) { _, _ -> requestPermission(fragment) }
            .show()
    }

    fun requestPermission(activity: Activity) {
        ActivityCompat.requestPermissions(activity, arrayOf(ACCESS_FINE_LOCATION), permissionsRequestCode)
    }

    fun requestPermission(fragment: Fragment) {
        fragment.requestPermissions(arrayOf(ACCESS_FINE_LOCATION), permissionsRequestCode)
    }

    /**
     * This should be called from the activity's {@link FragmentActivity#onRequestPermissionsResult(int, String[], int[])}
     *  (or the fragment's)
     */
    fun handleRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == permissionsRequestCode) {

            if (grantResults.isNotEmpty()) {
                if (grantResults.any { it == PackageManager.PERMISSION_DENIED }) {
                    callback?.onPermissionDenied()
                } else {
                    onPermissionGranted()
                }
            } else {
                callback?.onLocationSettingsDenied()
            }

        }
    }

    private fun onPermissionGranted() {
        checkLocationSettings()
    }

    fun checkLocationSettings() {
        state = State.REQ_LOCATION_SETTINGS
        // Check if the device's location settings are adequate for the app's needs
        LocationServices.getSettingsClient(context)
            .checkLocationSettings(locationSettingsRequest)
            .addOnCompleteListener(this)
    }

    override fun onComplete(p0: Task<LocationSettingsResponse>) {
        try {
            p0.getResult(ApiException::class.java)
            // All location settings are satisfied
            onLocationSettingsSatisfied()
        } catch (e: ApiException) {
            when (e.statusCode) {
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    // Location settings are not satisfied.
                    (e as? ResolvableApiException)?.let { requestLocationSettings(it) }
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    callback?.onLocationSettingsUnavailable()
                    Timber.i("Location settings are inadequate, and cannot be fixed here.")
                }
            }
        }
    }

    fun requestLocationSettings(activity: Activity, resolvableApiException: ResolvableApiException) {
        // Show the user a dialog to upgrade location settings
        try {
            // Show the dialog by calling startResolutionForResult(), and check the result
            // in onActivityResult().
            resolvableApiException.startResolutionForResult(activity, locationSettingsRequestCode)
        } catch (e: IntentSender.SendIntentException) {
            Timber.i("PendingIntent unable to execute request.")
        }
    }

    fun requestLocationSettings(fragment: Fragment, resolvableApiException: ResolvableApiException) {
        // Show the user a dialog to upgrade location settings
        try {
            // Show the dialog by calling startIntentSenderForResult(), and check the result
            // in onActivityResult().
            fragment.startIntentSenderForResult(
                resolvableApiException.resolution.intentSender,
                locationSettingsRequestCode,
                null,
                0,
                0,
                0,
                null
            )
        } catch (e: IntentSender.SendIntentException) {
            Timber.i("PendingIntent unable to execute request.")
        }
    }

    /**
     * This should be called in the activity's {@link Activity#onActivityResult(int, int, Intent)}
     *  (or fragment's)
     */
    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == locationSettingsRequestCode) {

            when (resultCode) {
                Activity.RESULT_OK -> {
                    Timber.i("User agreed to make required location settings changes.")
                    onLocationSettingsSatisfied()
                }
                Activity.RESULT_CANCELED -> {
                    callback?.onLocationSettingsDenied()
                    Timber.i("User chose not to make required location settings changes.")
                }
            }

        }
    }

    private fun onLocationSettingsSatisfied() {
        state = State.READY
        // sanity check
        if (!paused && ContextCompat.checkSelfPermission(
                context,
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startLocationUpdates()
        }
    }

    @SuppressWarnings("MissingPermission")
    private fun startLocationUpdates() {
        state = State.UPDATING

        LocationServices.getFusedLocationProviderClient(context)
            .requestLocationUpdates(actualLocationRequest, locationCallback, Looper.myLooper()!!)
            .addOnFailureListener { callback?.onConnectionFailed(it) }
    }

    private fun stopLocationUpdates() {
        state = State.READY
        LocationServices.getFusedLocationProviderClient(context).removeLocationUpdates(locationCallback)
    }


    interface Callback {
        fun onLocationChanged(location: Location?)

        fun onConnectionFailed(exception: Exception)

        fun onLocationSettingsUnavailable()

        fun onPermissionDenied()

        fun onLocationSettingsDenied()
    }
}