package com.tedmob.afrimoney.data.api.dto


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NawecFeesDTO(
    @field:[Expose SerializedName("dateNTime")]
    val dateNTime: String?,
    @field:[Expose SerializedName("MESSAGE")]
    val mESSAGE: String?,
    @field:[Expose SerializedName("meterNo")]
    val meterNo: String?,
    @field:[Expose SerializedName("meterSgc")]
    val meterSgc: String?,
    @field:[Expose SerializedName("receiptNo")]
    val receiptNo: String?,
    @field:[Expose SerializedName("serCharge")]
    val serCharge: String?,
    @field:[Expose SerializedName("siUnit")]
    val siUnit: String?,
    @field:[Expose SerializedName("TXNSTATUS")]
    val tXNSTATUS: String?,
    @field:[Expose SerializedName("tariffBreakdown")]
    val tariffBreakdown: List<TariffBreakdown?>?,
    @field:[Expose SerializedName("tariffName")]
    val tariffName: String?,
    @field:[Expose SerializedName("tenderAmt")]
    val tenderAmt: String?,
    @field:[Expose SerializedName("txnAmount")]
    val txnAmount: String?,
    @field:[Expose SerializedName("uniqueNumber")]
    val uniqueNumber: String?,
    @field:[Expose SerializedName("unitValue")]
    val unitValue: String?
) {
    data class TariffBreakdown(
        @field:[Expose SerializedName("q2ratesymbol")]
        val q2ratesymbol: String?,
        @field:[Expose SerializedName("q2ratevalue")]
        val q2ratevalue: String?,
        @field:[Expose SerializedName("q2unitssiUnit")]
        val q2unitssiUnit: String?,
        @field:[Expose SerializedName("q2unitsvalue")]
        val q2unitsvalue: String?
    )
}