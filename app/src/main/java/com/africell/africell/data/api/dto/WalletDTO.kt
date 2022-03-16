package com.africell.africell.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class WalletDTO(
        @field:[Expose SerializedName("id")]
        val id: String?,
        @field:[Expose SerializedName("name")]
        val name: String?,
        @field:[Expose SerializedName("description")]
        val description: String?

){
        override fun toString(): String {
                return name.orEmpty()
        }
}