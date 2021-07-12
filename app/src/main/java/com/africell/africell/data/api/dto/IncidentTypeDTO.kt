package com.africell.africell.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class IncidentTypeDTO(
    @field:[Expose SerializedName("idincidentType")]
    val idincidentType: Long?,
    @field:[Expose SerializedName("incidentTypeName")]
    val incidentTypeName: String?,
    @field:[Expose SerializedName("isActive")]
    val isActive: Int?,
    @field:[Expose SerializedName("languageid")]
    val languageid: Int?
){
    override fun toString(): String {
        return incidentTypeName.orEmpty()
    }
}