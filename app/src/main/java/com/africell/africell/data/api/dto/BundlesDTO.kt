package com.africell.africell.data.api.dto
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize


@Parcelize
data class BundlesDTO(
    @field:[Expose SerializedName("bundleInfo")]
    val bundleInfo: List<BundleInfo>?,
    @field:[Expose SerializedName("bundleTypeName")]
    val bundleTypeName: String?
):Parcelable