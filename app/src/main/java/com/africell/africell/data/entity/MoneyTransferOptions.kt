package com.africell.africell.data.entity

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes


data class MoneyTransferOptions(
    val id: IDS?,
    @DrawableRes val icon:Int,
    val name: String?
) {
    enum class IDS { P2P, AFRI_POWER, LINE_RECHARGE, BUNDLES, MONEY_TRANSFER, MERCHANT_PAYMENT }
}