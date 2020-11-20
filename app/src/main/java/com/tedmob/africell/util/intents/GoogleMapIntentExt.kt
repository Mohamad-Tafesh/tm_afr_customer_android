@file:JvmName("GoogleMapIntentUtils")

package com.tedmob.africell.util.intents

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment as SupportFragment

//Intents:

fun getGoogleMapIntent(latitude: Double, longitude: Double): Intent =
    getViewIntent("geo:=$latitude,$longitude")

fun getGoogleMapNavigationIntent(latitude: Double, longitude: Double): Intent =
    getViewIntent("google.navigation:q=$latitude,$longitude")

//...

//Activity:

fun Activity.openGoogleMap(latitude: Double, longitude: Double) {
    startActivity(getGoogleMapIntent(latitude, longitude))
}

fun Activity.openGoogleMapNavigation(latitude: Double, longitude: Double) {
    startActivity(getGoogleMapNavigationIntent(latitude, longitude))
}

//...

//Support fragment:

fun SupportFragment.openGoogleMap(latitude: Double, longitude: Double) {
    startActivity(getGoogleMapIntent(latitude, longitude))
}

fun SupportFragment.openGoogleMapNavigation(latitude: Double, longitude: Double) {
    startActivity(getGoogleMapNavigationIntent(latitude, longitude))
}

//...

//Context:

fun Context.openGoogleMap(latitude: Double, longitude: Double) {
    startActivity(getGoogleMapIntent(latitude, longitude))
}

fun Context.openGoogleMapNavigation(latitude: Double, longitude: Double) {
    startActivity(getGoogleMapNavigationIntent(latitude, longitude))
}

//...