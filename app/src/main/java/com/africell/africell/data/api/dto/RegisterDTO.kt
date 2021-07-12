package com.africell.africell.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class RegisterDTO(
        @field:[Expose SerializedName("resultCode")]
        val resultCode: Int?,
        @field:[Expose SerializedName("resultText")]
        val resultText: String?,
        @field:[Expose SerializedName("authenticationToken")]
        val authenticationToken: String?,
        @field:[Expose SerializedName("refreshToken")]
        val refreshToken: String?,

)