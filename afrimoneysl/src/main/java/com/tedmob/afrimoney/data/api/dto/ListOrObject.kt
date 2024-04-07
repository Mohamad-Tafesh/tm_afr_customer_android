package com.tedmob.afrimoney.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ListOrObject<T>(
    @field:[Expose SerializedName("list")]
    var list: List<T>
)
