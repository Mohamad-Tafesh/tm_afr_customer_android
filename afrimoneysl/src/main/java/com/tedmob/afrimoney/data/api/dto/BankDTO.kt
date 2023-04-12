package com.tedmob.afrimoney.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BankDTO(
    @field:[Expose SerializedName("DATA")] val data: Map<String, Any?>?,

    ) : CommandContainerDTO.Item()
