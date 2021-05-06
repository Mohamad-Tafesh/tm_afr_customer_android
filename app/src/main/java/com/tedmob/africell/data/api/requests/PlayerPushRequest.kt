package com.tedmob.africell.data.api.requests

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PlayerPushRequest(
    @field:[Expose SerializedName("playerId")]
    val playerId: String?
)