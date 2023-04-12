package com.tedmob.afrimoney.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AfricellServicesDTO(
    @field:[Expose SerializedName("BundleList")] val BundleList: List<Map<String, Any?>>?,
)