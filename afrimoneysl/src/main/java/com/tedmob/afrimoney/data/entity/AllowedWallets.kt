package com.tedmob.afrimoney.data.entity

import android.os.Parcelable
import com.tedmob.afrimoney.ui.spinner.searchable.MaterialSearchableSpinnerItem
import kotlinx.parcelize.Parcelize

@Parcelize
class AllowedWallets(val id:String,val name:String): MaterialSearchableSpinnerItem(), Parcelable {

    override fun toString(): String =
      name //also used for spinner's selected view

    override fun toDisplayString(): String =
        name //used for spinner's searchable dialog's items
}
