package com.tedmob.afrimoney.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TransactionDTO(
 @field:[Expose SerializedName("MESSAGE")] val mssge: MESSAGE?,
): CommandContainerDTO.Item(){

    class MESSAGE(
        @field:[Expose SerializedName("DATA")] val data: DATA?,
    ){
        class DATA(
            @field:[Expose SerializedName("TXNDT")] val date: List<String>?,
            @field:[Expose SerializedName("TXNID")] val transaction_id: List<String>?,
            @field:[Expose SerializedName("FROM")] val from: List<String>?,
            @field:[Expose SerializedName("TXNAMT")] val amount: List<String>?,
        )
    }


}