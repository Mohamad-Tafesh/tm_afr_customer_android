package com.tedmob.africell.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class BalanceDTO(
    @field:[Expose SerializedName("id")] val id: Int?,
    @field:[Expose SerializedName("name")] val title: String?
)