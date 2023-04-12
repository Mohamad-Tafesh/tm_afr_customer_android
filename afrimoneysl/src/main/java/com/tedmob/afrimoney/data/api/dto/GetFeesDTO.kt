package com.tedmob.afrimoney.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetFeesDTO(
 @field:[Expose SerializedName("status")] val status: String?,
 @field:[Expose SerializedName("errors")] val errors: List<AfrimoneyError>?,
 @field:[Expose SerializedName("message")] val message: Map<String, Any?>?,

)