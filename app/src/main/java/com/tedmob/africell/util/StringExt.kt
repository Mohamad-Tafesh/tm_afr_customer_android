package com.tedmob.africell.util

import java.text.SimpleDateFormat
import java.util.*

const val TIME_FORMAT_EVENT = "HH:mm"
const val APP_DATE_FORMAT = "EEE, MMM d, yy"
const val ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.sssX"
const val DOB_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"

fun String.substringMax(maxChars: Int): String {
    return substring(0..Math.min(maxChars, length - 1))
}

fun String.toTimeInMillis(dateformat: String): Long {
    try {
        val sdf = SimpleDateFormat(dateformat, Locale.US)
        val date: Date = sdf.parse(this)
        return date.time
    } catch (e: Exception) {
        return 0
    }
}

fun Long.convertDate(dateformat: String): String {
    val sdf = SimpleDateFormat(dateformat, Locale.US)
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    val date = sdf.format(this.toMillis())
    return date

}

fun Long.toEpoch(): Long = this / 1000
fun Long.toMillis(): Long = this * 1000
fun Long.toFormat(dateformat: String): String {
    val format1 = SimpleDateFormat(dateformat, Locale.US)
//    format1.timeZone = TimeZone.getTimeZone("UTC")
    val date = format1.format(this)
    return date
}

fun String.toFormat(dateformat: String): String {
    val format1 = SimpleDateFormat(dateformat, Locale.US)
//    format1.timeZone = TimeZone.getTimeZone("UTC")
    val date = format1.format(this)
    return date
}
