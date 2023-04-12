package com.tedmob.afrimoney.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RefreshTokenDTO(
    @field:[Expose SerializedName("access_token")] val accessToken: String,
    @field:[Expose SerializedName("token_type")] val tokenType: String?,
    @field:[Expose SerializedName("refresh_token")] val refreshToken: String,


)