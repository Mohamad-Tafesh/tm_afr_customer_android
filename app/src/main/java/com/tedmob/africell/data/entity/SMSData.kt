package com.tedmob.africell.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tedmob.africell.data.api.dto.SMSCountDTO

data class SMSData(
    val countries:List<Country>,
    val smsCount:SMSCountDTO
)