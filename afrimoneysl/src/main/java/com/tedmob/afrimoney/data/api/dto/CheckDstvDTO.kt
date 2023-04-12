package com.tedmob.afrimoney.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CheckDstvDTO(

    @field:[Expose SerializedName("dueAmount")] val dueAmount: String?,
    @field:[Expose SerializedName("dueDate")] val dueDate: String?,
    @field:[Expose SerializedName("txnStatus")] val txnStatus: String?,
    @field:[Expose SerializedName("MESSAGE")] val message: String?,

    )