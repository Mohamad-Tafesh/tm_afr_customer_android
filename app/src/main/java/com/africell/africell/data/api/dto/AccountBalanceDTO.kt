package com.africell.africell.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class AccountBalanceDTO(
    @field:[Expose SerializedName("accountStatmentInfo")]
    val accountStatmentInfo: AccountStatmentInfo?,
    @field:[Expose SerializedName("accountType")]
    val accountType: String?,
    @field:[Expose SerializedName("balance")]
    val balance: Balance?,
    @field:[Expose SerializedName("data")]
    val `data`: Data?,
    @field:[Expose SerializedName("freeBalance")]
    val freeBalance: FreeBalance?,
    @field:[Expose SerializedName("homePage")]
    val homePage: List<HomePage>?,
    @field:[Expose SerializedName("sms")]
    val sms: Sms?,
    @field:[Expose SerializedName("voice")]
    val voice: Voice?
) {
    data class AccountStatmentInfo(
        @field:[Expose SerializedName("crediLimit")]
        val crediLimit: String?
    )

    data class Balance(
        @field:[Expose SerializedName("currentBalance")]
        val currentBalance: String?,
        @field:[Expose SerializedName("maxValue")]
        val maxValue: String?,
        @field:[Expose SerializedName("title")]
        val title: String?,
        @field:[Expose SerializedName("unit")]
        val unit: String?
    )

    data class Data(
        @field:[Expose SerializedName("dataInfos")]
        val dataInfos: List<DataInfo>?,
        @field:[Expose SerializedName("title")]
        val title: String?
    )

    data class FreeBalance(
        @field:[Expose SerializedName("listFreeBalance")]
        val listFreeBalance: List<FreeBalanceX>?,
        @field:[Expose SerializedName("title")]
        val title: String?
    )

    data class HomePage(
        @field:[Expose SerializedName("expiryDate")]
        val expiryDate: String?,
        @field:[Expose SerializedName("name")]
        val name: String?,
        @field:[Expose SerializedName("originalValue")]
        val originalValue: String?,
        @field:[Expose SerializedName("unit")]
        val unit: String?,
        @field:[Expose SerializedName("value")]
        val value: String?
    )

    data class Sms(
        @field:[Expose SerializedName("smsInfos")]
        val smsInfos: List<SmsInfo>?,
        @field:[Expose SerializedName("title")]
        val title: String?
    )

    data class Voice(
        @field:[Expose SerializedName("title")]
        val title: String?,
        @field:[Expose SerializedName("voiceInfos")]
        val voiceInfos: List<VoiceInfo>?
    )

    data class DataInfo(
        @field:[Expose SerializedName("expiryDate")]
        val expiryDate: String?,
        @field:[Expose SerializedName("name")]
        val name: String?,
        @field:[Expose SerializedName("originalValue")]
        val originalValue: String?,
        @field:[Expose SerializedName("unit")]
        val unit: String?,
        @field:[Expose SerializedName("validity")]
        val validity: String?,
        @field:[Expose SerializedName("value")]
        val value: String?
    )

    data class FreeBalanceX(
        @field:[Expose SerializedName("balance")]
        val balance: String?,
        @field:[Expose SerializedName("balanceUnit")]
        val balanceUnit: String?,
        @field:[Expose SerializedName("description")]
        val description: String?,
        @field:[Expose SerializedName("image")]
        val image: String?,
        @field:[Expose SerializedName("title")]
        val title: String?
    )

    data class SmsInfo(
        @field:[Expose SerializedName("expiryDate")]
        val expiryDate: String?,
        @field:[Expose SerializedName("name")]
        val name: String?,
        @field:[Expose SerializedName("originalValue")]
        val originalValue: String?,
        @field:[Expose SerializedName("unit")]
        val unit: String?,
        @field:[Expose SerializedName("value")]
        val value: String?
    )

    data class VoiceInfo(
        @field:[Expose SerializedName("expiryDate")]
        val expiryDate: String?,
        @field:[Expose SerializedName("name")]
        val name: String?,
        @field:[Expose SerializedName("originalValue")]
        val originalValue: String?,
        @field:[Expose SerializedName("unit")]
        val unit: String?,
        @field:[Expose SerializedName("value")]
        val value: String?
    )
}