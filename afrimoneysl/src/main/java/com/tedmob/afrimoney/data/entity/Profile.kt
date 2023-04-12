package com.tedmob.afrimoney.data.entity

import java.util.*

class Profile(
    val userImage: String?,
    val firstName: String?,
    val lastName: String?,
    val genderValue: String?,
    val dob: String?,//Date?,
    val address: String?, //TODO address details?
    val idNumber: String?,
    val idType: String?,
    val frontImage: String?,
    val backImage: String?,
    val passportImage: String?,
) {
    companion object {
        const val DATE_FORMAT = "dd/MM/yyyy"
    }
}