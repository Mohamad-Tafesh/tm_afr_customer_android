package com.africell.africell.data.api.requests.afrimoney

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MerchantPayRequest(
    @field:[Expose SerializedName("walletName")]
    val walletName: String  ?,
    @field:[Expose SerializedName("merchantCode")]
    val merchantCode: String?,
    @field:[Expose SerializedName("mpin")]
    val mpin: String?,
    @field:[Expose SerializedName("amount")]
    val amount: String?,
)