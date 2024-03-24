package com.africell.africell.data.api.dto

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

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
    val volume: String?,
    @field:[Expose SerializedName("primaryColor")]
    val primaryColor: String?,
    @field:[Expose SerializedName("secondaryColor")]
    val secondaryColor: String?,
    @field:[Expose SerializedName("isAutoRenewActive")]
    val isAutoRenewActive: String?,
    @field:[Expose SerializedName("extraInfo")]
    val extraInfo: ExtraInfo?,

    var isFromMe: Boolean,
) : Parcelable {
    fun getFormatVolume(): String {
        return "$volume $unit"
    }

    fun getFormatValidity(): String {
        return "$volume$unit/$validity$validityUnit"
    }

    fun getTitle(): String {
        return "$category"
    }

    @Parcelize
    data class ExtraInfo(
        @field:[Expose SerializedName("left")]
        val left: Left?,
        @field:[Expose SerializedName("main")]
        val main: Main?,
        @field:[Expose SerializedName("right")]
        val right: Right?
    ) : Parcelable {
        @Parcelize
        data class Left(
            @field:[Expose SerializedName("title")]
            val title: String?,
            @field:[Expose SerializedName("value")]
            val value: String?
        ) : Parcelable

        @Parcelize
        data class Main(
            @field:[Expose SerializedName("title")]
            val title: String?,
            @field:[Expose SerializedName("value")]
            val value: String?
        ) : Parcelable

        @Parcelize
        data class Right(
            @field:[Expose SerializedName("title")]
            val title: String?,
            @field:[Expose SerializedName("value")]
            val value: String?
        ) : Parcelable
    }

}