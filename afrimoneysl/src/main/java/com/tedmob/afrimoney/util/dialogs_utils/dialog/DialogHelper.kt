package com.tedmob.taxibeirut.user.my_libs.dialogs_utils.dialog

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.viewbinding.ViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel

/** 2023年02月28日に中野夜とによって作成されました */
class DialogHelper<DVB : ViewBinding>(
    val context: Context,
    private var bindingProvider: (inflater: LayoutInflater) -> DVB,
) {

    private var isDialogCancellable: Boolean = true
    private var setupDialog: ((DVB, AlertDialog) -> Unit)? = null
    private var cornersValue: Float = 0F

    private fun showDialog(): AlertDialog {
        val view = LayoutInflater.from(context).let(bindingProvider)
        val builder = MaterialAlertDialogBuilder(context)

        val drawable = MaterialShapeDrawable(
            ShapeAppearanceModel.builder()
                .setAllCornerSizes(cornersValue)
                .build()
        ).apply {
            fillColor = ColorStateList.valueOf(Color.TRANSPARENT)
        }

        val alert = builder
            .setBackground(drawable)
            .setCancelable(isDialogCancellable)
            .setView(view.root)
            .show()

        setupDialog?.invoke(view, alert)

        return alert
    }

    /** Extension */
    fun <DVB : ViewBinding> DialogHelper<DVB>.buildDialog(value: (bindDialog: DVB, dialog: AlertDialog) -> Unit): AlertDialog {
        this.setupDialog = value
        return showDialog()
    }

    fun <DVB : ViewBinding> DialogHelper<DVB>.isCancellable(value: Boolean) {
        this.isDialogCancellable = value
    }

    fun <DVB : ViewBinding> DialogHelper<DVB>.setCorner(value: Float) {
        this.cornersValue = value
    }

}

