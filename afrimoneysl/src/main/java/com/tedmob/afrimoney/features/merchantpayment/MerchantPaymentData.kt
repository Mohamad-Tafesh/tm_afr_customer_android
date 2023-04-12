package com.tedmob.afrimoney.features.merchantpayment

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
class MerchantPaymentData(
    val merchantCode: String,
    val amount: String,
    val name: String,
    val fees:String,
    val total: String,
) : Parcelable