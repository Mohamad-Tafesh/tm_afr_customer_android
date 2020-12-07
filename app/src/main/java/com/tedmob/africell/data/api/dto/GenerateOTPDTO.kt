package com.tedmob.africell.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class GenerateOTPDTO(
        @field:[Expose SerializedName("resultCode")]
        val resultCode: Int?,
        @field:[Expose SerializedName("resultText")]
        val resultText: String?

)