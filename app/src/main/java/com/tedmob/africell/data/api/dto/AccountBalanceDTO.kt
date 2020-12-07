package com.tedmob.africell.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class AccountBalanceDTO(
    @field:[Expose SerializedName("accountInfo")]
    val accountInfo: AccountInfo?,
    @field:[Expose SerializedName("accountStatmentInfo")]
    val accountStatmentInfo: AccountStatmentInfo?,
    @field:[Expose SerializedName("accountType")]
    val accountType: String?,
    @field:[Expose SerializedName("data")]
    val mdata: List<Data>?,
    @field:[Expose SerializedName("sms")]
    val sms: List<Bundles>?,
    @field:[Expose SerializedName("voice")]
    val voice: List<Bundles>?
) {
    data class AccountInfo(
        @field:[Expose SerializedName("balance")]
        val balance: String?,
        @field:[Expose SerializedName("cos_name")]
        val cosName: String?,
        @field:[Expose SerializedName("cos_num")]
        val cosNum: String?,
        @field:[Expose SerializedName("creditLimit")]
        val creditLimit: String?,
        @field:[Expose SerializedName("cuG_Name")]
        val cuGName: String?,
        @field:[Expose SerializedName("cuG_Num")]
        val cuGNum: String?,
        @field:[Expose SerializedName("evC_Balance")]
        val evCBalance: String?,
        @field:[Expose SerializedName("first_used")]
        val firstUsed: String?,
        @field:[Expose SerializedName("free_money")]
        val freeMoney: String?,
        @field:[Expose SerializedName("free_sms")]
        val freeSms: String?,
        @field:[Expose SerializedName("free_time")]
        val freeTime: String?,
        @field:[Expose SerializedName("last_credited")]
        val lastCredited: String?,
        @field:[Expose SerializedName("last_outdial_call")]
        val lastOutdialCall: String?,
        @field:[Expose SerializedName("locked_flg")]
        val lockedFlg: String?,
        @field:[Expose SerializedName("loyalty_status")]
        val loyaltyStatus: String?,
        @field:[Expose SerializedName("msisdn")]
        val msisdn: String?
    )

    data class AccountStatmentInfo(
        @field:[Expose SerializedName("crediLimit")]
        val crediLimit: String?
    )

    data class Data(
        @field:[Expose SerializedName("allotment")]
        val allotment: String?,
        @field:[Expose SerializedName("expiresOn")]
        val expiresOn: String?,
        @field:[Expose SerializedName("productCatalogueOffer")]
        val productCatalogueOffer: String?,
        @field:[Expose SerializedName("remainder")]
        val remainder: String?,
        @field:[Expose SerializedName("validity")]
        val validity: String?
    )

    data class Bundles(
        @field:[Expose SerializedName("expiryDate")]
        val expiryDate: String?,
        @field:[Expose SerializedName("name")]
        val name: String?,
        @field:[Expose SerializedName("originalValue")]
        val originalValue: String?,
        @field:[Expose SerializedName("value")]
        val value: String?
    )
}