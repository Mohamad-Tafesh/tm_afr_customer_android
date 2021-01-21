package com.tedmob.africell.data.api.dto

import android.content.Context
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationDTO(



    @field:[Expose SerializedName("idShopLocation")]
    val idShopLocation: Int?,
    @field:[Expose SerializedName("languageid")]
    val languageid: Int?,
    @field:[Expose SerializedName("latitude")]
    val latitude: String?,
    @field:[Expose SerializedName("longitude")]
    val longitude: String?,
    @field:[Expose SerializedName("shopName")]
    val shopName: String?,
    @field:[Expose SerializedName("shopOwner")]
    val shopOwner: String?,
    @field:[Expose SerializedName("telephone")]
    val telephone: String?,
    @field:[Expose SerializedName("telephoneNumber")]
    val telephoneNumber: String?

) : Parcelable {
    fun displayDistance(): String {
        return /*distance?.let { String.format("$distance Km") } ?:*/ "N/A"
    }
}