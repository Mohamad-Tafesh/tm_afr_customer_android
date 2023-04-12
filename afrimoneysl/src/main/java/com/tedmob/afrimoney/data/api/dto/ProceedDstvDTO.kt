package com.tedmob.afrimoney.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProceedDstvDTO(
 @field:[Expose SerializedName("txnStatus")] val status: String?,
 @field:[Expose SerializedName("amount")] val amount: String?,
 @field:[Expose SerializedName("PriceUSD")] val priceUsd: String?,
 @field:[Expose SerializedName("message")] val message: String?,

)