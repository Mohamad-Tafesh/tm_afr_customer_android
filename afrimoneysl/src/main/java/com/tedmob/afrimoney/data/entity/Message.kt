package com.tedmob.afrimoney.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Message(
    val image: Int? = null,
    val messageTitle: String? = null,
    val messageDesc: String? = null
) : Parcelable