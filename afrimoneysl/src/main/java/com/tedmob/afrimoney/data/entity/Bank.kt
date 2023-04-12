package com.tedmob.afrimoney.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Bank(
    val primary: String,
    val bname: String,
    val bankid:String,
    val accnum:String,
    val isLinked: Boolean,
) : Parcelable