package com.tedmob.afrimoney.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FetchTokenDTO(
    @field:[Expose SerializedName("access_token")] val accessToken: String,
    @field:[Expose SerializedName("token_type")] val tokenType: String?,
    @field:[Expose SerializedName("expires_in")] val expiresIn: Long?,
    @field:[Expose SerializedName("scope")] val scope: String?,
    @field:[Expose SerializedName("jti")] val jti: String?,
)