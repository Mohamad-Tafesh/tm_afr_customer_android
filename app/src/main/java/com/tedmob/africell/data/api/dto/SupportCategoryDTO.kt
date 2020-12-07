package com.tedmob.africell.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SupportCategoryDTO(
    @field:[Expose SerializedName("categoryName")]
    val name: String?,
    @field:[Expose SerializedName("idsupportCategory")]
    val id: Long?,
    @field:[Expose SerializedName("isActive")]
    val isActive: Int?,
    @field:[Expose SerializedName("languageid")]
    val languageId: Int?
) {
    override fun toString(): String {
        return name.orEmpty()
    }
}