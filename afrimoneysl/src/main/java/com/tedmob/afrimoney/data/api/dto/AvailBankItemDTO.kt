package com.tedmob.afrimoney.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AvailBankItemDTO(
    @field:[Expose SerializedName("bankId")] val id: String?,
    @field:[Expose SerializedName("bankName")] val name: String?,

    ) : CommandContainerDTO.Item()
