package com.tedmob.africell.data.api.requests

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EditProfileRequest(

    @field:[Expose SerializedName("firstName")]
    val firstName: String?,
    @field:[Expose SerializedName("lastName")]
    val lastName: String?,
    @field:[Expose SerializedName("email")]
    val email: String?,
    @field:[Expose SerializedName("dateOfBirth")]
    val dateOfBirth: String?)