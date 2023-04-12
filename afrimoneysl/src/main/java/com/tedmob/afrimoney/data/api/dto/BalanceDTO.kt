package com.tedmob.afrimoney.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BalanceDTO(
    @field:[Expose SerializedName("PAYER_MLIMITCUST")] val PAYER_MLIMITCUST: String?,
    @field:[Expose SerializedName("PAYER_DLIMITCUST")] val PAYER_DLIMITCUST: String?,
    @field:[Expose SerializedName("FICBALANCE")] val FICBALANCE: String?,
    @field:[Expose SerializedName("TXNID")] val TXNID: String?,
    @field:[Expose SerializedName("BALANCE")] val BALANCE: String?,
    @field:[Expose SerializedName("IVR-RESPONSE")] val IVR_RESPONSE: String?,
    @field:[Expose SerializedName("PAYEE_DLIMITCUST")] val PAYEE_DLIMITCUST: String?,
    @field:[Expose SerializedName("OTHERWALLETS")] val OTHERWALLETS: String?,
    @field:[Expose SerializedName("FRBALANCE")] val FRBALANCE: String?,
    @field:[Expose SerializedName("PAYEE_MLIMITCUST")] val PAYEE_MLIMITCUST: String?,
): CommandContainerDTO.Item()