package com.tedmob.afrimoney.data.entity

import android.os.Parcelable
import com.tedmob.afrimoney.ui.spinner.searchable.MaterialSearchableSpinnerItem
import kotlinx.parcelize.Parcelize

@Parcelize
class Bundlelist(
    val BundleId: String,
    val Validity: String,
    val allowedReceiver: List<String>,
    val allowedWallets: List<AllowedWallets>,
    val description: String,
    val remark: String,
    val transactionAmount: String,
) : MaterialSearchableSpinnerItem(), Parcelable {


    override fun toString(): String =
        description + " " + Validity //also used for spinner's selected view

    override fun toDisplayString(): String =
        description + " " + Validity //used for spinner's searchable dialog's items
}