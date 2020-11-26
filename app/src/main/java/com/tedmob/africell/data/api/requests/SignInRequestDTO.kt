package com.tedmob.africell.data.api.requests

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SignInRequestDTO(
    @field:[Expose SerializedName("userName")]
    val username: String?,
    @field:[Expose SerializedName("password")]
    val password: String?
)