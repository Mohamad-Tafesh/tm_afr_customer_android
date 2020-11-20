package com.tedmob.africell.features.launch

import android.app.Activity
import androidx.navigation.NavController

open class NavigationAction(val act: NavController.(activity: Activity?) -> Unit)


inline fun NavController.navigateWithAction(action: NavigationAction, activity: Activity?) {
    action.act(this, activity)
}