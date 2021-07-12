package com.africell.africell.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Country(
        @field:[Expose SerializedName("code")] val code: String,
        @field:[Expose SerializedName("name")] val name: String,
        @field:[Expose SerializedName("dial_code")] val phonecode: String
) {
    override fun toString(): String = "$phonecode "

    companion object {
        const val LEBANON_CODE = "+961"
    }
}