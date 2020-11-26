package com.tedmob.africell.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SupportCategoryDTO(
    @field:[Expose SerializedName("id")] val id: Long,
    @field:[Expose SerializedName("name")] val name: String
){
    override fun toString(): String {
        return name
    }
}