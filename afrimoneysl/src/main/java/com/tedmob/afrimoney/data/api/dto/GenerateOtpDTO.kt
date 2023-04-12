package com.tedmob.afrimoney.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GenerateOtpDTO(
    @field:[Expose SerializedName("MSISDN")] val msisdn: String?,
    @field:[Expose SerializedName("OTP")] val otp: String?,
) : CommandContainerDTO.Item()