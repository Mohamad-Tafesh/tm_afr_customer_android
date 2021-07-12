package com.africell.africell.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MyBundlesAndServices(
    @field:[Expose SerializedName("myBundlesInfos")]
    val myBundlesInfos: List<ServicesDTO>?,
    @field:[Expose SerializedName("titles")]
    val titles: String?
)