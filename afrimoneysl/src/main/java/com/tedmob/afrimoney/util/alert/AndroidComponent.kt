@file:Suppress("unused")

package com.tedmob.afrimoney.util.alert

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import androidx.activity.result.ActivityResultRegistry
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

/**
 * The [AndroidComponent] represents an Android component for our app; it has the functions that are usually needed when accessing an
 * Android component. This makes it easier to add extensions to Android components without having multiple versions for each component.
 */
interface AndroidComponent {
    /**
     * @return The [Context] the Android component has access to.
     */
    val androidContext: Context?

    /**
     * @return The [Resources] the Android component has access to.
     */
    val androidResources: Resources

    fun startActivity(intent: Intent)
    fun startActivity(intent: Intent, options: Bundle?)

    /**
     * @return The [ActivityResultRegistry] the Android component has access to.
     */
    val androidResultRegistry: ActivityResultRegistry?

    //...
}


fun AppCompatActivity.toAndroidComponent() = object : AndroidComponent {
    override val androidContext: Context
        get() = this@toAndroidComponent

    override val androidResources: Resources
        get() = resources

    override fun startActivity(intent: Intent) {
        this@toAndroidComponent.startActivity(intent)
    }

    override fun startActivity(intent: Intent, options: Bundle?) {
        this@toAndroidComponent.startActivity(intent, options)
    }

    override val androidResultRegistry: ActivityResultRegistry
        get() = this@toAndroidComponent.activityResultRegistry
}

fun Fragment.toAndroidComponent() = object : AndroidComponent {
    override val androidContext: Context?
        get() = context

    override val androidResources: Resources
        get() = resources

    override fun startActivity(intent: Intent) {
        this@toAndroidComponent.startActivity(intent)
    }

    override fun startActivity(intent: Intent, options: Bundle?) {
        this@toAndroidComponent.startActivity(intent, options)
    }

    override val androidResultRegistry: ActivityResultRegistry?
        get() = this@toAndroidComponent.activity?.activityResultRegistry
}

fun Context.toAndroidComponent() = object : AndroidComponent {
    override val androidContext: Context
        get() = this@toAndroidComponent

    override val androidResources: Resources
        get() = resources

    override fun startActivity(intent: Intent) {
        this@toAndroidComponent.startActivity(intent)
    }

    override fun startActivity(intent: Intent, options: Bundle?) {
        this@toAndroidComponent.startActivity(intent, options)
    }

    override val androidResultRegistry: ActivityResultRegistry?
        get() = if (this@toAndroidComponent is AppCompatActivity) this@toAndroidComponent.activityResultRegistry else null
}