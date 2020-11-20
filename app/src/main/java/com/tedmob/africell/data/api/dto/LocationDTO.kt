package com.tedmob.africell.data.api.dto

import android.content.Context
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationDTO(
    @field:[Expose SerializedName("id")] val id: Int?,
    @field:[Expose SerializedName("name")] val title: String?,
    @field:[Expose SerializedName("description")] val description: String?,
    @field:[Expose SerializedName("lat")] val latitude: Double?,
    @field:[Expose SerializedName("lng")] val longitude: Double?,
    @field:[Expose SerializedName("distance")] val distance: Double?,
    @field:[Expose SerializedName("email")] val email: String?,
    @field:[Expose SerializedName("phone_nb")] val phoneNb: String?
) : Parcelable {
    fun displayDistance(): String {
        return distance?.let { String.format("$distance Km") } ?: "N/A"
    }
}