package com.africell.africell.util.navigation

import android.app.Activity
import androidx.navigation.NavController

fun NavController.goBack(activity: Activity?): Boolean {
    if (!popBackStack()) {
        activity?.finish()
    }
    return true
}

fun NavController.runIfFrom(destinationId: Int, block: (NavController.() -> Unit)) {
    if (currentDestination?.id == destinationId) {
        block(this)
    }

}