package com.tedmob.afrimoney.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserDTO(
    @field:[Expose SerializedName("LANGUAGE")] val language: String?,
    @field:[Expose SerializedName("ID_NUMBER")] val idNumber: String?,
    @field:[Expose SerializedName("MSISDN")] val msisdn: String?,
    @field:[Expose SerializedName("PARENTMSISDN")] val parentMSISDN: String?,
    @field:[Expose SerializedName("LNAME")] val lname: String?,
    @field:[Expose SerializedName("FNAME")] val fname: String?,
    @field:[Expose SerializedName("NAME")] val name: String?,
    @field:[Expose SerializedName("EMAIL")] val email: String?,
    @field:[Expose SerializedName("DOB")] val dob: String?,
    @field:[Expose SerializedName("GENDER")] val gender: String?, //Male
    @field:[Expose SerializedName("TRID")] val trId: String?,
    @field:[Expose SerializedName("OWNER_ID")] val ownerId: String?,
    @field:[Expose SerializedName("DETAIL")] val detail: Details?,


) : CommandContainerDTO.Item() {
    class Details(
        @field:[Expose SerializedName("CATEGORY_CODE")] val categoryCode: String?,
        @field:[Expose SerializedName("DEFAULT_CURRENCY")] val defaultCurrency: String?,
        @field:[Expose SerializedName("GRADE_CODE")] val gradeCode: String?,
        @field:[Expose SerializedName("USER_TYPE")] val userType: String?,
        @field:[Expose SerializedName("USER_ID")] val userId: String?,
        @field:[Expose SerializedName("PROVIDER_ID")] val providerId: String?,
        @field:[Expose SerializedName("DOMAINCODE")] val domainCode: String?,
        @field:[Expose SerializedName("PAYMENT_INSTRUMENT_ID")] val paymentInstrumentId: String?,
    )


    companion object {
        const val DATE_FORMAT = "dd-MMM-yyyy"
    }
}