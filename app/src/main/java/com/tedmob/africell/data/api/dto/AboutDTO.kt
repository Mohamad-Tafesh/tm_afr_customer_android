package com.tedmob.africell.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AboutDTO(
    @field:[Expose SerializedName("description")]
    val description: String?,
    @field:[Expose SerializedName("email")]
    val email: String?,
    @field:[Expose SerializedName("facebook")]
    val facebook: String?,
    @field:[Expose SerializedName("idaboutus")]
    val idaboutus: Int?,
    @field:[Expose SerializedName("image")]
    val image: String?,
    @field:[Expose SerializedName("languageid")]
    val languageid: Int?,
    @field:[Expose SerializedName("linkedin")]
    val linkedin: String?,
    @field:[Expose SerializedName("title")]
    val title: String?,
    @field:[Expose SerializedName("twitter")]
    val twitter: String?,
    @field:[Expose SerializedName("youtube")]
    val youtube: String?
)