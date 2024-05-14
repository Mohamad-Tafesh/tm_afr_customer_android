@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.tedmob.afrimoney.util.alert

import android.content.Context
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * This should be your only source for showing MaterialAlertDialogs in the entire app, that way you can customize it for the entire app.
 */
class MaterialAlertBuilder(
    context: Context,
    @StyleRes overrideThemeRes: Int = 0,
) : MaterialAlertDialogBuilder(context, overrideThemeRes) {
    private val onCreateRunnables = mutableListOf<AlertDialog.() -> Unit>()
    //private val onShowRunnables = mutableListOf<AlertDialog.() -> Unit>()


    fun overridePositiveButton(text: CharSequence?, onClick: AlertDialog.() -> Unit) {
        setPositiveButton(text, null)
        onCreateRunnables.add {
            getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener { onClick() }
        }
    }

    inline fun overridePositiveButton(@StringRes textRes: Int, noinline onClick: AlertDialog.() -> Unit) {
        overridePositiveButton(context.getString(textRes), onClick)
    }

    fun overrideNegativeButton(text: CharSequence?, onClick: AlertDialog.() -> Unit) {
        setNegativeButton(text, null)
        onCreateRunnables.add {
            getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener { onClick() }
        }
    }

    inline fun overrideNegativeButton(@StringRes textRes: Int, noinline onClick: AlertDialog.() -> Unit) {
        overrideNegativeButton(context.getString(textRes), onClick)
    }

    fun overrideNeutralButton(text: CharSequence?, onClick: AlertDialog.() -> Unit) {
        setNeutralButton(text, null)
        onCreateRunnables.add {
            getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener { onClick() }
        }
    }

    inline fun overrideNeutralButton(@StringRes textRes: Int, noinline onClick: AlertDialog.() -> Unit) {
        overrideNeutralButton(context.getString(textRes), onClick)
    }

    fun setWindowDimAmount(amount: Float) {
        onCreateRunnables.add {
            window?.setDimAmount(amount)
        }
    }

    //...


    override fun create(): AlertDialog {
        return super.create().also { alert ->
            onCreateRunnables.forEach { it(alert) }
        }
    }

    /*override fun show(): AlertDialog {
        return super.show()
    }*/
}


inline fun Context.materialAlert(
    @StyleRes overrideThemeRes: Int = 0,
    block: MaterialAlertBuilder.() -> Unit
): AlertDialog =
    MaterialAlertBuilder(this, overrideThemeRes).apply(block).create()

inline fun Context.showMaterialAlert(
    @StyleRes overrideThemeRes: Int = 0,
    block: MaterialAlertBuilder.() -> Unit
): AlertDialog =
    materialAlert(overrideThemeRes, block).also { it.show() }

inline fun Fragment.materialAlert(
    @StyleRes overrideThemeRes: Int = 0,
    block: MaterialAlertBuilder.() -> Unit
): AlertDialog? =
    activity?.let { MaterialAlertBuilder(it, overrideThemeRes).apply(block).create() }

inline fun Fragment.showMaterialAlert(
    @StyleRes overrideThemeRes: Int = 0,
    block: MaterialAlertBuilder.() -> Unit
): AlertDialog? =
    materialAlert(overrideThemeRes, block)?.also { it.show() }