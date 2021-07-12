package com.africell.africell.data.entity

import com.africell.africell.data.api.dto.SMSCountDTO

data class SMSData(
    val countries:List<Country>,
    val smsCount:SMSCountDTO
)