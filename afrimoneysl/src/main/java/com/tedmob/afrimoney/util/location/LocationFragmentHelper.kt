package com.tedmob.foras.util.location

import android.app.Activity
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.ResolvableApiException


class LocationFragmentHelper(private val fragment: Fragment, parentActivity: Activity) :
    LocationHelper(parentActivity) {

    var pauseInBackground = true

    init {
        permissionsRequestCode = 1337
        locationSettingsRequestCode = 1338
    }

    override fun onPermissionRequired() {
        onPermissionRequired(fragment)
    }

    override fun requestLocationSettings(resolvableApiException: ResolvableApiException) {
        requestLocationSettings(fragment, resolvableApiException)
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