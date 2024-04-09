package com.tedmob.afrimoney.features.bills.nawec

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
class GetNawecFeesData(
    val number: String,
    val amount: String,
    val name: String,
/*    val fees: String?,
    val name: String?,
    val unitValue: String,
    val siUnit: String,
    val uniqueNumber: String,
    val dateNTime: String,*/
) : Parcelable