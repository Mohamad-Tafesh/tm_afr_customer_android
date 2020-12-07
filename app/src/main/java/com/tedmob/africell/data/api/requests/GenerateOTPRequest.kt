package com.tedmob.africell.data.api.requests

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GenerateOTPRequest(
    @field:[Expose SerializedName("msisdn")]
    val msisdn: String?,
    @field:[Expose SerializedName("typeofOTP")]
    val typeofOTP: Int?
)