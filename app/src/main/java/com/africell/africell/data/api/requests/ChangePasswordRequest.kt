package com.africell.africell.data.api.requests

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChangePasswordRequest(
    @field:[Expose SerializedName("oldPassword")]
    val oldPassword: String?,
    @field:[Expose SerializedName("newPassword")]
    val newPassword: String?,
    @field:[Expose SerializedName("confirmNewPassword")]
    val confirmNewPassword: String?
)