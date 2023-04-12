package com.tedmob.afrimoney.notification

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotificationData(
    val title: String?,
    val message: String?,
    val image: String?,
    val imageWidth: Double? = null,
    val imageHeight: Double? = null,
    val url: String? = null
) : Parcelable