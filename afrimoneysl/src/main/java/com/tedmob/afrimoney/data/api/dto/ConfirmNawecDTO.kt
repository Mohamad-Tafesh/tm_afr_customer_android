package com.tedmob.afrimoney.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ConfirmNawecDTO(
    @field:[Expose SerializedName("COMMAND")] val command: COMMAND?
)

data class COMMAND(
    @field:[Expose SerializedName("TXNSTATUS")] val status: String?,
    @field:[Expose SerializedName("errors")] val errors: List<AfrimoneyError>?,
    @field:[Expose SerializedName("MESSAGE")] val message: String?,


    )