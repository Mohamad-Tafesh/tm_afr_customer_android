package com.tedmob.africell.data.api.dto

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BundleInfo(
    @field:[Expose SerializedName("activate")]
    val activate: Boolean?,
    @field:[Expose SerializedName("bundleId")]
    val bundleId: Long?,
    @field:[Expose SerializedName("category")]
    val category: String?,
    @field:[Expose SerializedName("commercialName")]
    val commercialName: String?,
    @field:[Expose SerializedName("image")]
    val image: String?,
    @field:[Expose SerializedName("price")]
    val price: String?,
    @field:[Expose SerializedName("subCategory")]
    val subCategory: String?,
    @field:[Expose SerializedName("subTitles")]
    val subTitles: String?,
    @field:[Expose SerializedName("unit")]
    val unit: String?,
    @field:[Expose SerializedName("validity")]
    val validity: String?,
    @field:[Expose SerializedName("validityUnit")]
    val validityUnit: String?,
    @field:[Expose SerializedName("volume")]
    val volume: String?
) : Parcelable {
    fun getFormatVolume(): String {
        return volume + " " + unit
    }
    fun getFormatValidity(): String {
        return volume + unit+ "/"+ validity + validityUnit
    }
    fun getTitle():String{
        return  "$category Bundles"
    }

}