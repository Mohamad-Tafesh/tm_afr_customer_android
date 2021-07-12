package com.africell.africell.data.api.requests

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RefreshTokenRequest(
    @field:[Expose SerializedName("authenticationToken")]
    val authenticationToken: String?,
    @field:[Expose SerializedName("refreshToken")]
    val refreshToken: String?
)