package com.tedmob.afrimoney.util.location.gms

import com.google.android.gms.location.LocationRequest
import com.tedmob.afrimoney.util.location.AppLocationRequest

inline fun AppLocationRequest.toGMSLocationRequest(): LocationRequest =
    LocationRequest.create()
        .setPriority(
            when (priority) {
                AppLocationRequest.Priority.BalancedPowerAccuracy -> LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
                AppLocationRequest.Priority.HighAccuracy -> LocationRequest.PRIORITY_HIGH_ACCURACY
                AppLocationRequest.Priority.LowPower -> LocationRequest.PRIORITY_LOW_POWER
                AppLocationRequest.Priority.NoPower -> LocationRequest.PRIORITY_NO_POWER
            }
        )
        .setInterval(interval)
        .setFastestInterval(fastestInterval)
        .setExpirationTime(expirationTime)
        .setNumUpdates(numUpdates)
        .setSmallestDisplacement(smallestDisplacement)
        .setMaxWaitTime(maxWaitTime)
