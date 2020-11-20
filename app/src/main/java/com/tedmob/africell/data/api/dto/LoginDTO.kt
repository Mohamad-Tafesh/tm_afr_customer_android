package com.tedmob.africell.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginDTO(
        @field:[Expose SerializedName("access_token")]
        val accessToken: String,
        @field:[Expose SerializedName("name")]
        val name: String,
        @field:[Expose SerializedName("email")]
        val email: String,
        @field:[Expose SerializedName("mobile_number")]
        val phoneNumber: String
)
