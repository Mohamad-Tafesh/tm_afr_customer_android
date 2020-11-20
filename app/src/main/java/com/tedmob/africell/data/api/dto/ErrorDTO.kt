package com.tedmob.africell.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ErrorDTO {
    @field:[Expose SerializedName("error")]
    var error: ApiError? = null

    data class ApiError(
            @field:[Expose SerializedName("code")]
            var code: Int = 0,
            @field:[Expose SerializedName("message")]
            var message: String? = null,
            @field:[Expose SerializedName("debugger")]
            var debugger: String? = null
    )
}