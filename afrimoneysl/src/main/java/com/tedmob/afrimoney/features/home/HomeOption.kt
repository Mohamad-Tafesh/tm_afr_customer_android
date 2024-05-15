package com.tedmob.afrimoney.features.home

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.tedmob.afrimoney.R

sealed class HomeOption(
    val imageData: Any?,
    @StringRes val label: Int,
    @IdRes val destination: Int,
    val arguments: Bundle? = null,
) {
    object TransferMoney :
        HomeOption(
            R.drawable.option_transfer_money,
            R.string.send_moneyh,
            R.id.nav_transfer_money
        )

    object PendingTransactions : HomeOption(
        R.drawable.option_pending_transactions,
        R.string.pending_transactionsh,
        R.id.nav_pending_transactions
    )

    object WithdrawMoney :
        HomeOption(R.drawable.option_withdraw_money, R.string.withdraw_moneyh, R.id.nav_withdraw)

    object AfricellServices :
        HomeOption(
            R.drawable.option_africell_services,
            R.string.africell_servicesh,
            R.id.nav_services
        )

    object Africredit : HomeOption(R.drawable.option_africredit, R.string.africredit, 0)

    object PayMyBills :
        HomeOption(R.drawable.option_pay_my_bills, R.string.pay_my_billsh, R.id.nav_pay_my_bills)

    object MerchantPayment : HomeOption(
        R.drawable.option_merchant_payment,
        R.string.merchant_paymenth,
        R.id.nav_merchant_payment
    )

    object BankingServices :
        HomeOption(
            R.drawable.option_banking_services,
            R.string.banking_servicesh,
            R.id.nav_banking_services
        )

    object BuyAirtime :
        HomeOption(R.drawable.option_airtime, R.string.buy_airtimeh, R.id.nav_airtime)


}