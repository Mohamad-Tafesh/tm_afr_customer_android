package com.tedmob.afrimoney.data.api.dto

import android.location.Location
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

@Parcelize
data class LocationDTO(


    @field:[Expose SerializedName("idShopLocation")]
    val idShopLocation: Int?,
    @field:[Expose SerializedName("languageid")]
    val languageid: Int?,
    @field:[Expose SerializedName("latitude")]
    val latitude: String?,
    @field:[Expose SerializedName("description")]
    val description: String?,
    @field:[Expose SerializedName("longitude")]
    val longitude: String?,
    @field:[Expose SerializedName("shopName")]
    val shopName: String?,
    @field:[Expose SerializedName("address")]
    val address: String?,
    @field:[Expose SerializedName("shopOwner")]
    val shopOwner: String?,
    @field:[Expose SerializedName("telephone")]
    val telephone: String?,
    @field:[Expose SerializedName("telephoneNumber")]
    val telephoneNumber: String?

) : Parcelable {

    fun getDistance(userLatitude: Double?, userLongitude: Double?):Double{
        return try {
            val lat = latitude?.toDoubleOrNull()
            val lng = longitude?.toDoubleOrNull()
            if (userLatitude != null && userLongitude != null && lng != null && lat != null) {
                val startPoint = Location("locationA")
                startPoint.latitude = userLatitude
                startPoint.longitude = userLongitude

                val endPoint = Location("locationB")
                endPoint.latitude = lat
                endPoint.longitude = lng

                val distanceMeters: Float = startPoint.distanceTo(endPoint)
                distanceMeters / 1000.00
            } else 0.0
        } catch (e: Exception) {
            0.0
        }
    }

    fun displayDistance(userLatitude: Double?, userLongitude: Double?): String {
        return try {
            val lat = latitude?.toDoubleOrNull()
            val lng = longitude?.toDoubleOrNull()
            if (userLatitude != null && userLongitude != null && lng != null && lat != null) {
                val startPoint = Location("locationA")
                startPoint.latitude = userLatitude
                startPoint.longitude = userLongitude

                val endPoint = Location("locationB")
                endPoint.latitude = lat
                endPoint.longitude = lng

                val distanceMeters: Float = startPoint.distanceTo(endPoint)
                val distanceKilometers = distanceMeters / 1000.00

                val df = DecimalFormat("#,##0.##", DecimalFormatSymbols(Locale.ENGLISH))

                df.format(distanceKilometers).toString() + "KM"
            } else ""
        } catch (e: Exception) {
            ""
        }
    }



    fun numbers(): List<String> {
        val numbers = mutableListOf<String>()
        if (telephoneNumber != null) {
            numbers.add(telephoneNumber)
        }
        if (telephone != null) {
            numbers.add(telephone)
        }
        return numbers
    }
}