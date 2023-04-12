package com.tedmob.afrimoney.util.navigation

import android.app.Activity
import android.view.Menu
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavGraph

fun NavController.goBack(activity: Activity?): Boolean {
    if (!popBackStack()) {
        activity?.finish()
    }
    return true
}

inline fun Menu.getStartDestinationsFor(navGraph: NavGraph): List<Int> {
    return (0 until size()).map {
        try {
            navGraph
                .findNode(getItem(it).itemId)!!.let {
                    if (it is NavGraph) {
                        it.startDestinationId
                    } else {
                        it.id
                    }
                }
        } catch (e: Exception) {
            getItem(it).itemId
        }
    }
}

inline fun NavController.runIfFrom(@IdRes destinationId: Int, block: NavController.() -> Unit) {
    if (currentDestination?.id == destinationId) {
        block()
    }
}