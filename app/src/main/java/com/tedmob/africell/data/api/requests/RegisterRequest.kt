package com.tedmob.africell.data.api.requests

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @field:[Expose SerializedName("username")]
    val username: String?,
    @field:[Expose SerializedName("firstName")]
    val firstName: String?,
    @field:[Expose SerializedName("lastName")]
    val lastName: String?,
    @field:[Expose SerializedName("email")]
    val email: String?,
    @field:[Expose SerializedName("dateOfBirth")]
    val dateOfBirth: String?,
    @field:[Expose SerializedName("password")]
    val password: String?,
    @field:[Expose SerializedName("confirmPassword")]
    val confirmPassword: String?
)