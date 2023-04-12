package com.tedmob.afrimoney.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginDTO(
        @field:[Expose SerializedName("TOKEN")] val token: String,
        @field:[Expose SerializedName("REFRESHTOKEN")] val refreshToken: String,
): CommandContainerDTO.Item()
