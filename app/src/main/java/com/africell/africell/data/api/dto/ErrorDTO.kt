package com.africell.africell.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ErrorDTO(
    @field:[Expose SerializedName("Status",alternate = ["status"])]
    val status: Int?,
    @field:[Expose SerializedName("Title",alternate = ["title"])]
    val title: String?
)