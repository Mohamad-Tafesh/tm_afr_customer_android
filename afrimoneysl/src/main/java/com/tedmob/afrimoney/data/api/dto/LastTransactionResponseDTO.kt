package com.tedmob.afrimoney.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LastTransactionResponseDTO(
    @field:[Expose SerializedName("DATA")] val data: Data?,
): CommandContainerDTO.Item() {
    class Data(
        @field:[Expose SerializedName("TRANSACTIONDATA")] val transactionData: TransactionData?,
    )

    class TransactionData(
        @field:[Expose SerializedName("ATTR1VALUE")] val attr1Value: List<String>?,
        @field:[Expose SerializedName("PAYID")] val payId: List<String>?,
        @field:[Expose SerializedName("FIRSTNAME")] val firstName: List<String>?,
        @field:[Expose SerializedName("FROM")] val from: List<String>?,
        @field:[Expose SerializedName("TXNTYPE")] val txnType: List<String>?,
        @field:[Expose SerializedName("TXNID")] val txnId: List<String>?,
        @field:[Expose SerializedName("TXNAMT")] val txnAmt: List<String>?,
        @field:[Expose SerializedName("TXNDT")] val txnDt: List<String>?,
        @field:[Expose SerializedName("ATTR3VALUE")] val attr3Value: List<String>?,
        @field:[Expose SerializedName("LASTNAME")] val lastName: List<String>?,
        @field:[Expose SerializedName("ATTR2VALUE")] val attr2Value: List<String>?,
        @field:[Expose SerializedName("SERVICE")] val service: List<String>?,
        @field:[Expose SerializedName("TXNSTATUS")] val txnStatus: List<String>?,
    )
}