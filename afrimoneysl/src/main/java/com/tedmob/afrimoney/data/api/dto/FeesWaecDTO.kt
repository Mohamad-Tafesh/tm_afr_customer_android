package com.tedmob.afrimoney.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FeesWaecDTO(
    @field:[Expose SerializedName("WAECFeeList")] val WAECFeeList: List<WaecFeeListDTO>?,
)