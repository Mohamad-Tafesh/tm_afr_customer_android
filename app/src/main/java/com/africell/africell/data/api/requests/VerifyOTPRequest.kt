package com.africell.africell.data.api.requests

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VerifyOTPRequest(
    @field:[Expose SerializedName("msisdn")]
    val msisdn: String?,
    @field:[Expose SerializedName("otpCode")]
    val otpCode: String?
)