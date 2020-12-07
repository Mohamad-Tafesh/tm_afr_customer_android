package com.tedmob.africell.data.entity
import com.google.gson.annotations.SerializedName

import com.google.gson.annotations.Expose


data class SubAccount(
    val account: String?,
    val isMain: Boolean?
){
    override fun toString(): String {
        return account.orEmpty()
    }
}