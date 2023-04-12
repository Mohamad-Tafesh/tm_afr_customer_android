package com.tedmob.afrimoney.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BankBalanceEnquiryDTO(
  @field:[Expose SerializedName("COMMAND")]
  var commad: Command,
  ){
  class Command(
    @field:[Expose SerializedName("TXNSTATUS")]
    var status: String? = null,

    @field:[Expose SerializedName("MESSAGE")]
    var message: String? = null,
  )
}