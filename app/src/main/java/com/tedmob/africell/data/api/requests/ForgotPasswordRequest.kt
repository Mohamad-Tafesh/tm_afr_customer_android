package com.tedmob.africell.data.api.requests

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ForgotPasswordRequest(
    @field:[Expose SerializedName("userName")]
    val userName: String?,
    @field:[Expose SerializedName("newPassword")]
    val newPassword: String?,
    @field:[Expose SerializedName("confirmNewPassword")]
    val confirmNewPassword: String?
)