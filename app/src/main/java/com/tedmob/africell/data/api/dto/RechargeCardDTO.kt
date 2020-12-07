package com.tedmob.africell.data.api.dto
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose


data class RechargeCardDTO(
    @field:[Expose SerializedName("idrechargecard")]
    val idRechargeCard: Long?,
    @field:[Expose SerializedName("isActive")]
    val isActive: Int?,
    @field:[Expose SerializedName("languageid")]
    val languageId: Int?,
    @field:[Expose SerializedName("rechargecardDescription")]
    val rechargeCardDescription: String?,
    @field:[Expose SerializedName("rechargecardImagename")]
    val rechargeCardImageName: String?,
    @field:[Expose SerializedName("rechargecardName")]
    val rechargeCardName: String?,
    @field:[Expose SerializedName("rechargecardPrice")]
    val rechargeCardPrice: String?
)