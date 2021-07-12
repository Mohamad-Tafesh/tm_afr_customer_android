package com.africell.africell.notification

import android.os.Parcel
import android.os.Parcelable

data class NotificationDataImpl(
    val title: String?,
    val message: String?,
    val image: String?,
    val imageWidth: Double? = null,
    val imageHeight: Double? = null
) : NotificationData {

    override fun title(): String? = title

    override fun message(): String? = message

    override fun image(): String? = image

    override fun imageWidth(): Double? = imageWidth

    override fun imageHeight(): Double? = imageHeight

    override fun type(): Int = 0


    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as Double?,
        parcel.readValue(Double::class.java.classLoader) as Double?
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(message)
        parcel.writeString(image)
        parcel.writeValue(imageWidth)
        parcel.writeValue(imageHeight)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<NotificationDataImpl> {
        override fun createFromParcel(parcel: Parcel): NotificationDataImpl =
            NotificationDataImpl(parcel)

        override fun newArray(size: Int): Array<NotificationDataImpl?> = arrayOfNulls(size)
    }
}