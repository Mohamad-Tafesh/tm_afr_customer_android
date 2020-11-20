@file:JvmName("AppNavigationUI")

package com.tedmob.africell.util.navigation

/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.view.MenuItem
import android.view.View
import androidx.annotation.IdRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavOptions
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView

@JvmOverloads
fun NavigationView.setupWithNavController(
    navController: NavController,
    customNavigationListener: ((menuItem: MenuItem) -> Boolean)?,
    customHasPriority: Boolean = false
) {
    setNavigationItemSelectedListener { item ->
        var handled: Boolean
        if (customHasPriority) {
            handled = customNavigationListener?.invoke(item) ?: false
            if (!handled) {
                handled = NavigationUI.onNavDestinationSelected(item, navController)
            }
        } else {
            handled = NavigationUI.onNavDestinationSelected(item, navController)
            if (!handled && customNavigationListener != null) {
                handled = customNavigationListener(item)
            }
        }

        if (handled) {
            if (parent is DrawerLayout) {
                (parent as? DrawerLayout)?.closeDrawer(this)
            } else {
                val bottomSheetBehavior = findBottomSheetBehavior(this)
                bottomSheetBehavior?.setState(BottomSheetBehavior.STATE_HIDDEN)
            }
        }

        handled
    }

    navController.addOnDestinationChangedListener { _, destination, _ ->
        val menu = menu

        for (h in 0 until menu.size()) {
            val item = menu.getItem(h)
            item.isChecked = matchDestination(destination, item.itemId)
        }
    }
}

//TODO other components handling from NavigationUI

/**
 * Attempt to navigate to the [NavDestination] associated with the given MenuItem. This
 * MenuItem should have been added via one of the helper methods in this class.
 *
 *
 * Importantly, it assumes the [menu item id][MenuItem.getItemId] matches a valid
 * [action id][NavDestination.getAction] or
 * [destination id][NavDestination.getId] to be navigated to.
 *
 * @param item          The selected MenuItem.
 * @param navController The NavController that hosts the destination.
 * @param popUp         Whether the pop backstack behavior should return to the start of the graph or not
 * @return True if the [NavController] was able to navigate to the destination
 * associated with the given MenuItem.
 */
@JvmOverloads
internal fun NavController.onNavDestinationSelected(item: MenuItem, popUp: Boolean = false): Boolean {
    return if (popUp) {
        onNavDestinationSelected(item.itemId, popUp)
    } else {
        NavigationUI.onNavDestinationSelected(item, this)
    }
}

/**
 * Attempt to navigate to the [NavDestination] associated with the given MenuItem. This
 * MenuItem should have been added via one of the helper methods in this class.
 *
 *
 * Importantly, it assumes the [menu item id][MenuItem.getItemId] matches a valid
 * [action id][NavDestination.getAction] or
 * [destination id][NavDestination.getId] to be navigated to.
 *
 * @param itemId        The selected MenuItem's id.
 * @param navController The NavController that hosts the destination.
 * @param popUp         Whether the pop backstack behavior should return to the start of the graph or not
 * @return True if the [NavController] was able to navigate to the destination
 * associated with the given MenuItem.
 */
@JvmOverloads
internal fun NavController.onNavDestinationSelected(
    @IdRes itemId: Int,
    popUp: Boolean = false
): Boolean {
    val options = getDefaultNavOptions(popUp)
    try {
        //TODO provide proper API instead of using Exceptions as Control-Flow.
        navigate(itemId, null, options)
        return true
    } catch (e: IllegalArgumentException) {
        return false
    }
}

val NavController.defaultNavOptions: NavOptions
    get() = getDefaultNavOptions(false)

val NavController.defaultSideMenuNavOptions: NavOptions
    get() = getDefaultNavOptions(true)

fun NavController.getDefaultNavOptions(popUp: Boolean): NavOptions =
    NavOptions.Builder()
        .setLaunchSingleTop(true)
        .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
        .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
        .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
        .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
        .apply {
            if (popUp) {
                findStartDestination(graph)?.id?.let { setPopUpTo(it, false) }
            }
        }.build()

/**
 * Determines whether the given `destId` matches the NavDestination. This handles
 * both the default case (the destination's id matches the given id) and the nested case where
 * the given id is a parent/grandparent/etc of the destination.
 */
internal fun matchDestination(destination: NavDestination, @IdRes destId: Int): Boolean {
    var currentDestination: NavDestination? = destination

    while (currentDestination?.id != destId && currentDestination?.parent != null) {
        currentDestination = currentDestination.parent
    }

    return currentDestination?.id == destId
}

/**
 * Finds the actual start destination of the graph, handling cases where the graph's starting
 * destination is itself a NavGraph.
 */
internal fun findStartDestination(graph: NavGraph): NavDestination? {
    var startDestination: NavDestination? = graph

    while (startDestination is NavGraph) {
        val parent: NavGraph? = startDestination
        startDestination = parent?.findNode(parent.startDestination)
    }

    return startDestination
}

/**
 * Walks up the view hierarchy, trying to determine if the given View is contained within
 * a bottom sheet.
 */
internal fun findBottomSheetBehavior(view: View): BottomSheetBehavior<out View>? {
    val params = view.layoutParams
    if (params !is CoordinatorLayout.LayoutParams) {
        val parent = view.parent
        return if (parent is View) findBottomSheetBehavior(parent) else null
    }

    val behavior = params.behavior
    return if (behavior !is BottomSheetBehavior) {
        // We hit a CoordinatorLayout, but the View doesn't have the BottomSheetBehavior
        null
    } else behavior
}

