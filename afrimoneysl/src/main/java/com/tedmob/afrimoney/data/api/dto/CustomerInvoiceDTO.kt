package com.tedmob.afrimoney.data.api.dto


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CustomerInvoiceDTO(
    @field:[Expose SerializedName("MESSAGE")]
    val mESSAGE: String?,
    @field:[Expose SerializedName("receiptlist")]
    val receiptlist: List<Receiptlist>?,
    @field:[Expose SerializedName("TXNSTATUS")]
    val tXNSTATUS: String?
) {
    data class Receiptlist(
        @field:[Expose SerializedName("amount")]
        val amount: String?,
        @field:[Expose SerializedName("dateTime")]
        val dateTime: String?,
        @field:[Expose SerializedName("receiptNo")]
        val receiptNo: String?,
        @field:[Expose SerializedName("token")]
        val token: String?,
        @field:[Expose SerializedName("valueUnit")]
        val valueUnit: String?
    )
}