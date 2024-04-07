package com.tedmob.afrimoney.data.api.dto


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CustomerDTO(
    @field:[Expose SerializedName("Address")]
    val address: String?,
    @field:[Expose SerializedName("BirthDate")]
    val birthDate: String?,
    @field:[Expose SerializedName("DOC_BACK_BASE64")]
    val dOCBACKBASE64: String?,
    @field:[Expose SerializedName("DOC_FRONT_BASE64")]
    val dOCFRONTBASE64: String?,
    @field:[Expose SerializedName("FirstName")]
    val firstName: String?,
    @field:[Expose SerializedName("Gender")]
    val gender: String?,
    @field:[Expose SerializedName("Language")]
    val language: Int?,
    @field:[Expose SerializedName("LastName")]
    val lastName: String?,
    @field:[Expose SerializedName("Nationality")]
    val nationality: String?,
    @field:[Expose SerializedName("OfficialDOcKey")]
    val officialDOcKey: String?,
    @field:[Expose SerializedName("OfficialDOcName")]
    val officialDOcName: String?,
    @field:[Expose SerializedName("OfficialDOcSerialNumber")]
    val officialDOcSerialNumber: String?,
    @field:[Expose SerializedName("Prefix")]
    val prefix: String?,
    @field:[Expose SerializedName("regType")]
    val regType: String?,
    @field:[Expose SerializedName("Status")]
    val status: String?,
    @field:[Expose SerializedName("statusMsg")]
    val statusMsg: String?
)