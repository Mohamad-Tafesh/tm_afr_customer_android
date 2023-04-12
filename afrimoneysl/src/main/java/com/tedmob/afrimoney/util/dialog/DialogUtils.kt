package com.tedmob.afrimoney.util.dialog

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import com.tedmob.afrimoney.R

object DialogUtils {

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

    fun loadingDialog(context: Context): ProgressDialog {
        return loadingDialog(context, context.getString(R.string.loading_))
    }

    fun showLoadingDialog(context: Context): ProgressDialog {
        val dialog = loadingDialog(context)
        dialog.show()
        return dialog
    }

    fun showLoadingDialog(context: Context, dialog: ProgressDialog?): ProgressDialog {
        var dialogTemp = dialog
        if (dialogTemp == null) {
            dialogTemp = loadingDialog(context)
        }
        if (!dialogTemp.isShowing) {
            dialogTemp.show()
        }
        return dialogTemp
    }

    fun tryDismissDialog(dialog: Dialog?) {
        dialog?.dismiss()
    }
}