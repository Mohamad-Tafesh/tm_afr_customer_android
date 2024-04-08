package com.tedmob.afrimoney.data.api.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PendingTransactionsData(
    val TXNDT: String,
    val SERVICEREQUESTID: String,
    val PAYID: String,
    val LASTNAME: String,
    val FIRSTNAME: String,
    val FROM: String,
    val TO: String,
    val TXNID: String,
    val TXNAMT: String,
    val SERVICETYPE: String,
    val SERVICENAME: String,
) : Parcelable