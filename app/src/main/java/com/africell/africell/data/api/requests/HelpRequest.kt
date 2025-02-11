package com.africell.africell.data.api.requests

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HelpRequest(
    @field:[Expose SerializedName("supportCategoryid")]
    val supportCategoryid: Long?,
    @field:[Expose SerializedName("supportCategoryName")]
    val supportCategoryName: String?,
    @field:[Expose SerializedName("message")]
    val message: String?
)