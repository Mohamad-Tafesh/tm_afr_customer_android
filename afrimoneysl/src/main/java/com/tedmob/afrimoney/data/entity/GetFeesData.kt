package com.tedmob.afrimoney.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
class GetFeesData(
    val number: String,
    val amount: String,
    val fees: String,
    val name: String?,
    val total: String,
) : Parcelable