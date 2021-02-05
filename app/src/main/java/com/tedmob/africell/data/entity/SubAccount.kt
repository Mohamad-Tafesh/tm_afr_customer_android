package com.tedmob.africell.data.entity
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

import com.google.gson.annotations.Expose
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