package com.africell.africell.data.api.dto
import com.google.gson.annotations.SerializedName

import com.google.gson.annotations.Expose


data class SMSCountDTO(
    @field:[Expose SerializedName("resultCode")]
    val resultCode: Int?,
    @field:[Expose SerializedName("resultText")]
    val resultText: String?,
    @field:[Expose SerializedName("smsCount")]
    val smsCount: Int?
)