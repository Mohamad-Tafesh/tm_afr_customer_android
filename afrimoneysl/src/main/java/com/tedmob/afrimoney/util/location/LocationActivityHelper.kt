package com.tedmob.foras.util.location

import android.app.Activity
import com.google.android.gms.common.api.ResolvableApiException
import com.tedmob.foras.util.location.LocationHelper


class LocationActivityHelper(private val activity: Activity) : LocationHelper(activity) {

    var pauseInBackground = true

    init {
        permissionsRequestCode = 1337
        locationSettingsRequestCode = 1338
    }

    override fun onPermissionRequired() {
        onPermissionRequired(activity)
    }

    override fun requestLocationSettings(resolvableApiException: ResolvableApiException) {
        requestLocationSettings(activity, resolvableApiException)
    }

    fun onPause() {
        if (pauseInBackground && state >= State.READY) {
            paused = true
        }
    }

    fun onResume() {
        paused = false
    }
}