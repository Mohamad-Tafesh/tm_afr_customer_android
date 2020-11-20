package com.tedmob.africell.data.api.dto

import android.content.Context
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class ServicesDTO(
    @field:[Expose SerializedName("id")] val id: Int?,
    @field:[Expose SerializedName("name")] val title: String?,
    @field:[Expose SerializedName("is_subscribed")] val isSubscribed:Boolean?
)