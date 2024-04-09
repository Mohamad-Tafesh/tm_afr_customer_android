package com.tedmob.afrimoney.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ClientNawecDTO(
@field:[Expose SerializedName("TYPE")] val name: String?
):CommandContainerDTO.Item()