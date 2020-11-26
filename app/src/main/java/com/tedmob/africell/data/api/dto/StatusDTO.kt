package com.tedmob.africell.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class StatusDTO(
        @field:[Expose SerializedName("msg",alternate = ["status"])] val msg: String?
)