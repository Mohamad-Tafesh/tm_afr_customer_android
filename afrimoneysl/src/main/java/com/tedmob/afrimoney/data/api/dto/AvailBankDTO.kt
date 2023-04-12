package com.tedmob.afrimoney.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AvailBankDTO(
    @field:[Expose SerializedName("bankList")] val list: List<AvailBankItemDTO>?,

    ) : CommandContainerDTO.Item()
