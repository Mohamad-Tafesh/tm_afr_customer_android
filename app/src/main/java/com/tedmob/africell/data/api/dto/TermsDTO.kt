package com.tedmob.africell.data.api.dto


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TermsDTO(
    @field:[Expose SerializedName("created_at")]
    val createdAt: Long,
    @field:[Expose SerializedName("description")]
    val description: String?,
    @field:[Expose SerializedName("id")]
    val id: Long?,
    @field:[Expose SerializedName("image")]
    val image: String?,
    @field:[Expose SerializedName("title")]
    val title: String?
)