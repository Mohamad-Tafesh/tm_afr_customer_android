package com.africell.africell.features.faq
import com.google.gson.annotations.SerializedName

import com.google.gson.annotations.Expose

data class FaqItem(
    @field:[Expose SerializedName("idfaqs")]
    val id: Int?,
    @field:[Expose SerializedName("isActive")]
    val isActive: Int?,
    @field:[Expose SerializedName("languageid")]
    val languageid: Int?,
    @field:[Expose SerializedName("questions")]
    val question: String?,
    @field:[Expose SerializedName("response")]
    val response: String?
)