package com.africell.africell.data.entity
import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize
data class SubAccount(
    val account: String?,
    val isMain: Boolean?
):Parcelable{
    override fun toString(): String {
        return account.orEmpty()
    }
}