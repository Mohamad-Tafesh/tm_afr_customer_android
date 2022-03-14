package com.africell.africell.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MoneyTransferBalanceDTO(
    @field:[Expose SerializedName("balanceValue")]
    val balanceValue: String?,
    @field:[Expose SerializedName("currency")]
    val currency: String?,
    @field:[Expose SerializedName("subtitle")]
    val subtitle: String?,
    @field:[Expose SerializedName("walletName")]
    val walletName: String?
)