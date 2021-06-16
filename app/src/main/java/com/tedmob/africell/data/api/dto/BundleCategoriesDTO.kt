package com.tedmob.africell.data.api.dto

import android.content.Context
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class BundleCategoriesDTO(
    @field:[Expose SerializedName("categoryName")]
    val categoryName: String?,
    @field:[Expose SerializedName("idbundlecategories")]
    val idBundleCategories: Long?,
    @field:[Expose SerializedName("image")]
    val image: String?,
    @field:[Expose SerializedName("languageId")]
    val languageId: Int?,
    @field:[Expose SerializedName("status")]
    val status: Int?,
    @field:[Expose SerializedName("primaryColor")]
    val primaryColor: String?,
    @field:[Expose SerializedName("secondaryColor")]
    val secondaryColor: String?,
):Parcelable
