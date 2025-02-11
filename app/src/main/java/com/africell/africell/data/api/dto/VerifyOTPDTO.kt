package com.africell.africell.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class VerifyOTPDTO(
        @field:[Expose SerializedName("resultCode")]
        val resultCode: Int?,
        @field:[Expose SerializedName("resultText")]
        val resultText: String?,
        @field:[Expose SerializedName("verificationToken")]
        val verificationToken: String?

)