package com.tedmob.afrimoney.util.dialogs_utils.dialog_bottom

import android.content.Context
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tedmob.afrimoney.R


class DialogBottomHelper<DVB : ViewBinding>(val context: Context, private var bindingProvider: (inflater: LayoutInflater) -> DVB) {

    private var isDialogCancellable: Boolean = true
    private var setupDialog: ((DVB, BottomSheetDialog) -> Unit)? = null
    private var cornersValue: Float = 0F

    private fun showDialog(): BottomSheetDialog {
        val view = LayoutInflater.from(context).let(bindingProvider)
        val dialog = BottomSheetDialog(context, R.style.Dialog_Transparent)

        dialog.setCancelable(isDialogCancellable)
        dialog.setContentView(view.root)
        setupDialog?.invoke(view, dialog)
        dialog.show()

        return dialog
    }

    /** Extension */
    fun <DVB : ViewBinding> DialogBottomHelper<DVB>.buildDialog(value: (bindDialog: DVB, dialog: BottomSheetDialog) -> Unit): BottomSheetDialog {
        this.setupDialog = value
        return showDialog()
    }

    fun <DVB : ViewBinding> DialogBottomHelper<DVB>.isCancellable(value: Boolean) {
        this.isDialogCancellable = value
    }

    fun <DVB : ViewBinding> DialogBottomHelper<DVB>.setCorner(value: Float) {
        this.cornersValue = value
    }

}

