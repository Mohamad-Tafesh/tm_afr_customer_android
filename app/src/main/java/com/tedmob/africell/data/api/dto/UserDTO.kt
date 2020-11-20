package com.tedmob.africell.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class UserDTO(
    @field:[Expose SerializedName("age")]
    val age: String?,
    @field:[Expose SerializedName("country")]
    val country: Country?,
    @field:[Expose SerializedName("country_code")]
    val countryCode: String?,
    @field:[Expose SerializedName("dob")]
    val dob: String?,
    @field:[Expose SerializedName("email")]
    val email: String?,
    @field:[Expose SerializedName("first_name")]
    val firstName: String?,
    @field:[Expose SerializedName("gender")]
    val gender: String?,
    @field:[Expose SerializedName("id")]
    val id: Long?,
    @field:[Expose SerializedName("is_new_user")]
    val isNewUser: Boolean?,
    @field:[Expose SerializedName("last_name")]
    val lastName: String?,
    @field:[Expose SerializedName("photo")]
    val photo: String?,
    @field:[Expose SerializedName("primary_phone")]
    val primaryPhone: String?,
    @field:[Expose SerializedName("token")]
    val token: String?,
    @field:[Expose SerializedName("member_token")]
    val memberToken: String?
) {

    data class Country(
        @field:[Expose SerializedName("calling_code")]
        val callingCode: String?,
        @field:[Expose SerializedName("capital")]
        val capital: String?,
        @field:[Expose SerializedName("citizenship")]
        val citizenship: String?,
        @field:[Expose SerializedName("country_code")]
        val countryCode: String?,
        @field:[Expose SerializedName("currency")]
        val currency: String?,
        @field:[Expose SerializedName("currency_code")]
        val currencyCode: String?,
        @field:[Expose SerializedName("currency_decimals")]
        val currencyDecimals: Int?,
        @field:[Expose SerializedName("currency_sub_unit")]
        val currencySubUnit: String?,
        @field:[Expose SerializedName("currency_symbol")]
        val currencySymbol: String?,
        @field:[Expose SerializedName("eea")]
        val eea: Int?,
        @field:[Expose SerializedName("email")]
        val email: String?,
        @field:[Expose SerializedName("flag")]
        val flag: String?,
        @field:[Expose SerializedName("full_name")]
        val fullName: String?,
        @field:[Expose SerializedName("id")]
        val id: Int?,
        @field:[Expose SerializedName("iso_3166_2")]
        val iso31662: String?,
        @field:[Expose SerializedName("iso_3166_3")]
        val iso31663: String?,
        @field:[Expose SerializedName("name")]
        val name: String?,
        @field:[Expose SerializedName("region_code")]
        val regionCode: String?,
        @field:[Expose SerializedName("sub_region_code")]
        val subRegionCode: String?,
        @field:[Expose SerializedName("visibility")]
        val visibility: Int?
    )
}
