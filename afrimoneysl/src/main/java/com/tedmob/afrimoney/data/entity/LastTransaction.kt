package com.tedmob.afrimoney.data.entity

class LastTransaction(
    val attr1Value: String?,
    val payId: String?,
    val firstName: String?,
    val from: String?,
    val txnType: String?,
    val txnId: String?,
    val txnAmt: String?,
    val txnDt: String?,
    val attr3Value: String?,
    val lastName: String?,
    val attr2Value: String?,
    val service: String?,
    val txnStatus: String?,
) {
    companion object {
        const val SERVER_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss.S"
    }
}