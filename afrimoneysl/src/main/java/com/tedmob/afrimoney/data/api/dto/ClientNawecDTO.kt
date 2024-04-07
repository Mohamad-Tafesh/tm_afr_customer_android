package com.tedmob.afrimoney.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ClientNawecDTO(
    @field:[Expose SerializedName("TXNSTATUS")] val txnStatus: String?,
    @field:[Expose SerializedName("MESSAGE")] val message: String?,
@field:[Expose SerializedName("custName")] val name: String?
)