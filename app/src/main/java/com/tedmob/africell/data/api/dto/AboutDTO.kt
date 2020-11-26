package com.tedmob.africell.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AboutDTO(
    @field:[Expose SerializedName("created_at")]
    val createdAt: Long?,
    @field:[Expose SerializedName("description")]
    val description: String?,
    @field:[Expose SerializedName("email")]
    val email: String?,
    @field:[Expose SerializedName("facebook")]
    val facebook: String?,
    @field:[Expose SerializedName("linkedin")]
    val linkedIn: String?,
    @field:[Expose SerializedName("id")]
    val id: Int?,
    @field:[Expose SerializedName("image")]
    val image: String?,
    @field:[Expose SerializedName("instagram")]
    val instagram: String?,
    @field:[Expose SerializedName("message")]
    val message: String?,
    @field:[Expose SerializedName("title")]
    val title: String?,
    @field:[Expose SerializedName("twitter")]
    val twitter: String?,
    @field:[Expose SerializedName("website")]
    val website: String?,
    @field:[Expose SerializedName("youtube")]
    val youtube: String?
)