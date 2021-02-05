package com.tedmob.africell.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class DataCalculatorDTO(
    @field:[Expose SerializedName("bundleSuggestion")]
    val bundleSuggestion: List<BundleSuggestion>?,
    @field:[Expose SerializedName("datacalculators")]
    val datacalculators: List<DataCalculator>?
) {

    data class BundleSuggestion(
        @field:[Expose SerializedName("activate")]
        val activate: Boolean?,
        @field:[Expose SerializedName("bundleId")]
        val bundleId: Int?,
        @field:[Expose SerializedName("category")]
        val category: String?,
        @field:[Expose SerializedName("commercialName")]
        val commercialName: String?,
        @field:[Expose SerializedName("image")]
        val image: String?,
        @field:[Expose SerializedName("price")]
        val price: String?,
        @field:[Expose SerializedName("subCategory")]
        val subCategory: String?,
        @field:[Expose SerializedName("unit")]
        val unit: String?,
        @field:[Expose SerializedName("validity")]
        val validity: String?,
        @field:[Expose SerializedName("validityUnit")]
        val validityUnit: String?,
        @field:[Expose SerializedName("volume")]
        val volume: String?
    ){
        fun getFormatVolume(): String {
            return volume + " " + unit
        }
        fun getFormatValidity(): String {
            return volume + unit+ "/"+ validity + validityUnit
        }
    }

    data class DataCalculator(
        @field:[Expose SerializedName("costPerUnit")]
        val costPerUnit: String?,
        @field:[Expose SerializedName("variableUnit")]
        val variableUnit: String?,
        @field:[Expose SerializedName("descriptions")]
        val descriptions: String?,
        @field:[Expose SerializedName("idDataCalculator")]
        val idDataCalculator: String,
        @field:[Expose SerializedName("languageid")]
        val languageid: Int?,
        @field:[Expose SerializedName("maximumValue")]
        val maximumValue: String?,
        @field:[Expose SerializedName("minimumValue")]
        val minimumValue: String,
        @field:[Expose SerializedName("name")]
        val name: String?
    )
}