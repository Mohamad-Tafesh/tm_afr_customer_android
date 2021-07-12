package com.africell.africell.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class UserDTO(
    @field:[Expose SerializedName("dateOfBirth")]
    val dateOfBirth: String?,
    @field:[Expose SerializedName("email")]
    val email: String?,
    @field:[Expose SerializedName("firstName")]
    val firstName: String?,
    @field:[Expose SerializedName("lastName")]
    val lastName: String?,
    @field:[Expose SerializedName("resultCode")]
    val resultCode: Int?,
    @field:[Expose SerializedName("resultText")]
    val resultText: String?
)