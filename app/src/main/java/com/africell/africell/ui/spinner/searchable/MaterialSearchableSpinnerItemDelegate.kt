package com.africell.africell.ui.spinner.searchable

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

class MaterialSearchableSpinnerItemDelegate(private val item: Any?) : MaterialSearchableSpinnerItem() {
    override fun toDisplayString(): String = item.toString()
}

@Parcelize
class MaterialSearchableSpinnerParcelableItemDelegate(private val item: Parcelable?) :
    MaterialSearchableSpinnerItem(), Parcelable {

    override fun toDisplayString(): String = item.toString()
}