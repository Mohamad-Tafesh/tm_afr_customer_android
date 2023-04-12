package com.tedmob.afrimoney.data.entity

import android.os.Parcelable
import com.tedmob.afrimoney.ui.spinner.searchable.MaterialSearchableSpinnerItem
import kotlinx.parcelize.Parcelize

@Parcelize
open class ComboEntry(
    val id: String,
    val label: String,
) : MaterialSearchableSpinnerItem(), Parcelable {

    override fun toString(): String = label
    override fun toDisplayString(): String = label
}