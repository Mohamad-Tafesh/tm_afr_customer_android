package com.tedmob.afrimoney.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AfrimoneyError(
    @field:[Expose SerializedName("message")] val message: String?,
) {
}