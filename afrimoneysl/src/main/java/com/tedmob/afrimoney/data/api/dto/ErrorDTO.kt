package com.tedmob.afrimoney.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ErrorDTO(
    @field:[Expose SerializedName("COMMAND")] val error: ApiError?,
) {
    data class ApiError(
        @field:[Expose SerializedName("TXNSTATUS")] val code: Int,
        @field:[Expose SerializedName("MESSAGE")] val message: String?,
        @field:[Expose SerializedName("debugger")] val debugger: String?,
    )
}