package com.africell.africell.data.api.dto
import com.google.gson.annotations.SerializedName

import com.google.gson.annotations.Expose


data class SubAccountDTO(
    @field:[Expose SerializedName("mainAccount")]
    val mainAccount: String?,
    @field:[Expose SerializedName("resultCode")]
    val resultCode: Int?,
    @field:[Expose SerializedName("resultText")]
    val resultText: String?,
    @field:[Expose SerializedName("subAccount")]
    val subAccount: List<String>?
)