package com.tedmob.afrimoney.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AppSettingsDTO(
    @field:[Expose SerializedName("android_force_update")] val androidForceUpdate: Boolean?,
    @field:[Expose SerializedName("android_version")] val androidVersion: String?,
    @field:[Expose SerializedName("force_update_title")] val forceUpdateTitle: String?,
    @field:[Expose SerializedName("force_update_message")] val forceUpdateMessage: String?,
)