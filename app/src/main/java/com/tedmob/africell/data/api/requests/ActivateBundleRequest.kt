package com.tedmob.africell.data.api.requests

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ActivateBundleRequest(
    @field:[Expose SerializedName("bundleId")]
    val bundleId: Int?,
    @field:[Expose SerializedName("isAutoRenew")]
    val isAutoRenew: Boolean?,
    @field:[Expose SerializedName("senderMSISDN")]
    val senderMSISDN: String?,
    @field:[Expose SerializedName("targetMSISDN")]
    val targetMSISDN: String?
)