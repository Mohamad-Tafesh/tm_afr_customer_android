package com.tedmob.africell.data.api.dto

import android.content.Context
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class UsefulNumberDTO(
    @field:[Expose SerializedName("description")]
    val description: String?,
    @field:[Expose SerializedName("idusefulnumbers")]
    val idusefulnumbers: Long?,
    @field:[Expose SerializedName("isActive")]
    val isActive: Int?,
    @field:[Expose SerializedName("languageid")]
    val languageid: Int?,
    @field:[Expose SerializedName("name")]
    val name: String?,
    @field:[Expose SerializedName("number")]
    val number: String?
)