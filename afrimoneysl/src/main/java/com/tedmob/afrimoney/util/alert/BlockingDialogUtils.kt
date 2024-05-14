@file:Suppress("DEPRECATION")

package com.tedmob.afrimoney.util.alert

import android.app.ProgressDialog
import android.content.Context

@Suppress("MemberVisibilityCanBePrivate")
object BlockingDialogUtils {

    fun loadingDialog(context: Context, message: String): ProgressDialog {
        val dialog = ProgressDialog(context)
        dialog.setMessage(message)
        dialog.setCancelable(false)
        return dialog
    }

    fun showLoadingDialog(context: Context, message: String): ProgressDialog {
        val dialog = loadingDialog(context, message)
        dialog.show()
        return dialog
    }
}