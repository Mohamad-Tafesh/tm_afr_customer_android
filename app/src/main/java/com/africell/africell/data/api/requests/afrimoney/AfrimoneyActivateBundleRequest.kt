package com.africell.africell.data.api.requests.afrimoney

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AfrimoneyActivateBundleRequest(
    @field:[Expose SerializedName("bundleId")]
    val bundleId: String?,
    @field:[Expose SerializedName("walletName")]
    val walletName: String?,
    @field:[Expose SerializedName("senderMsisdn")]
    val senderMSISDN: String?,
    @field:[Expose SerializedName("receiverMsisdn")]
    val receiverMsisdn: String?,
    @field:[Expose SerializedName("mpin")]
    val mpin: String?,
)