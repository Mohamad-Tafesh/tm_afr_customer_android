package com.tedmob.afrimoney.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RegisterDTO(
    @field:[Expose SerializedName("CUSTID")] val custid: String?,
    @field:[Expose SerializedName("TXNID")] val txnId: String?,
    @field:[Expose SerializedName("TRID")] val trId: String?,
): CommandContainerDTO.Item()