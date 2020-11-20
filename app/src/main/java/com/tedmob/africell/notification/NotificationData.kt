package com.tedmob.africell.notification

import android.os.Parcelable

interface NotificationData : Parcelable {
    fun title(): String?
    fun message(): String?
    fun image(): String?
    fun imageWidth(): Double?
    fun imageHeight(): Double?
    fun type(): Int
}