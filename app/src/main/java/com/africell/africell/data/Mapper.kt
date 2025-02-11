package com.africell.africell.data

import com.africell.africell.data.api.dto.AccountBalanceDTO
import com.africell.africell.data.api.dto.MoneyTransferBalanceDTO
import com.africell.africell.data.entity.MyAccountBalance
import com.africell.africell.data.entity.MyAccountBalanceCategories


fun AccountBalanceDTO.toAccountBalanceCategories() :MutableList<MyAccountBalanceCategories>{
val list= mutableListOf<MyAccountBalanceCategories>()
    list.apply {
        sms?.smsInfos?.let {
            add(MyAccountBalanceCategories(sms.title,it.map { MyAccountBalance(it.name,it.expiryDate,it.value,it.originalValue,it.unit) }.toMutableList()))
        }
        data?.dataInfos?.let { add(MyAccountBalanceCategories(data.title,it.map { MyAccountBalance(it.name,it.expiryDate,it.value,it.originalValue,it.unit) }))}
        voice?.voiceInfos?.let { add(MyAccountBalanceCategories(voice.title,it.map { MyAccountBalance(it.name,it.expiryDate,it.value,it.originalValue,it.unit) }))}
    }
    return list



}

fun List<AccountBalanceDTO.HomePage>.toHomeBalance():MutableList<MyAccountBalance>{
    return this.map {  MyAccountBalance(it.name,it.expiryDate,it.value,it.originalValue,it.unit)}.toMutableList()

}


fun List<MoneyTransferBalanceDTO>.MMtoHomeBalance():MutableList<MyAccountBalance>{
    return this.map {  MyAccountBalance(it.walletName,it.subtitle,it.balanceValue,it.balanceValue,it.currency)}.toMutableList()

}