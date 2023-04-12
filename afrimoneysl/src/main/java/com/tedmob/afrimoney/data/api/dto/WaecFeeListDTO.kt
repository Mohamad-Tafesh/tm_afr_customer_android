package com.tedmob.afrimoney.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WaecFeeListDTO(
    @field:[Expose SerializedName("Currency")] val Currency: String?,
    @field:[Expose SerializedName("FeeAmount")] val FeeAmount: String?,
    @field:[Expose SerializedName("FeeId")] val FeeId: String?,
    @field:[Expose SerializedName("FeeType")] val FeeType: String?,
)