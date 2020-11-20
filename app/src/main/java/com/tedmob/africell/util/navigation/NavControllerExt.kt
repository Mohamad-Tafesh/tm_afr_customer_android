package com.tedmob.africell.util.navigation

import android.app.Activity
import androidx.navigation.NavController

fun NavController.goBack(activity: Activity?): Boolean {
    if (!popBackStack()) {
        activity?.finish()
    }
    return true
}