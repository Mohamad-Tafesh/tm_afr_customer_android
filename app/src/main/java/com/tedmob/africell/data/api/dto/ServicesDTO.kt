package com.tedmob.africell.data.api.dto

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ServicesDTO(
    @field:[Expose SerializedName("sname")]
    val sname: String?,
    @field:[Expose SerializedName("buttonLabel")]
    val buttonLabel: String?,
    @field:[Expose SerializedName("canUnsbscribe")]
    val canUnsbscribe: Boolean?,
    @field:[Expose SerializedName("currentValue")]
    val currentValue: String?,
    @field:[Expose SerializedName("description")]
    val description: String?,
    @field:[Expose SerializedName("expiryDate")]
    val expiryDate: String?,
    @field:[Expose SerializedName("image")]
    val image: String?,
    @field:[Expose SerializedName("isActive")]
    val isActive: Boolean?,
    @field:[Expose SerializedName("isServices")]
    val isServices: Boolean?,
    @field:[Expose SerializedName("maxValue")]
    val maxValue: String?,
    @field:[Expose SerializedName("activateDate")]
    val activateDate: String?,
    @field:[Expose SerializedName("name")]
    val name: String?,
    @field:[Expose SerializedName("price")]
    val price: String?,
    @field:[Expose SerializedName("priceUnit")]
    val priceUnit: String?,
    @field:[Expose SerializedName("subTitles",alternate = ["subTitle"])]
    val subTitle: String?,
    @field:[Expose SerializedName("validity")]
    val validity: String?
) : Parcelable
