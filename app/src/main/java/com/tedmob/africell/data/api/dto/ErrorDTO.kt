package com.tedmob.africell.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ErrorDTO(
    @field:[Expose SerializedName("status")]
    val status: Int?,
    @field:[Expose SerializedName("title")]
    val title: String?,
    @field:[Expose SerializedName("traceId")]
    val traceId: String?,
    @field:[Expose SerializedName("type")]
    val type: String?
)