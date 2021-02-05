package com.tedmob.africell.data.api.dto


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TermsDTO(
    @field:[Expose SerializedName("description")]
    val description: String?,
    @field:[Expose SerializedName("idterms")]
    val idterms: Long?,
    @field:[Expose SerializedName("image")]
    val image: String?,
    @field:[Expose SerializedName("languageid")]
    val languageid: Int?,
    @field:[Expose SerializedName("title")]
    val title: String?
)