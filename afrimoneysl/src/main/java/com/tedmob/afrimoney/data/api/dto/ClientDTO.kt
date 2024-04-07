package com.tedmob.afrimoney.data.api.dto


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class ClientDTO(

    @SerializedName("COMMAND")
    @Expose val commad:COMMAND,
    //@Expose val commad: Map<String, Any?>?,

){
    data class COMMAND(
        @SerializedName("DETAILS")
        @Expose val clientDetails: ListOrObject<ClientDetails>?,
        @SerializedName("TXNSTATUS")
        @Expose val status: String?,
    )
}