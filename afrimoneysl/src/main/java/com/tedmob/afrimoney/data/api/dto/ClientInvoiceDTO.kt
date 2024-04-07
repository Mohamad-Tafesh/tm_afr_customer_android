package com.tedmob.afrimoney.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ClientInvoiceDTO(
    @field:[Expose SerializedName("TXNSTATUS")] var status: String? = null,
    @field:[Expose SerializedName("MESSAGE")] var message: String? = null,
    @field:[Expose SerializedName("TotalDebt")] val totalDebt: String?,
    @field:[Expose SerializedName("PendingInvoices")] val pendingInvoices: List<Details>?,
) {
    class Details(
        @field:[Expose SerializedName("DocNo")] val docNo: String?,
        @field:[Expose SerializedName("Year")] val year: String?,
        @field:[Expose SerializedName("Month")] val month: String?,
        @field:[Expose SerializedName("AmountFinal")] val amountFinal: String?,
    )

}