package com.tedmob.africell.data.api.dto

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class MyBundlesAndServices(
    @field:[Expose SerializedName("myBundlesInfos")]
    val myBundlesInfos: List<ServicesDTO>?,
    @field:[Expose SerializedName("titles")]
    val titles: String?
)