package com.tedmob.afrimoney.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PressDTO(
    @field:[Expose SerializedName("SubscriptionList")] val data: List<Map<String,Any?>>?,
)
