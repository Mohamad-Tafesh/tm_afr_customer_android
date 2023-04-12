package com.tedmob.afrimoney.data.api.dto

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class PendingTransactionsItemDTO(
    @field:[Expose SerializedName("transferDate")] val date: String?,
    @field:[Expose SerializedName("serviceRequestId")] val serviceRequestId: String?,
    @field:[Expose SerializedName("transferId")] val transaction_id: String?,
    @field:[Expose SerializedName("counterPartyMsisdn")] val from: String?,
    @field:[Expose SerializedName("transferAmount")] val amount: String?,


    ) : Parcelable