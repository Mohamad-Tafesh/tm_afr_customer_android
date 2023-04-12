package com.tedmob.afrimoney.features.merchantpayment.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.features.merchantpayment.MerchantPaymentData
import javax.inject.Inject

class GetFeesUseCase
@Inject constructor(
    private val api: TedmobApis,
) : SuspendableUseCase<MerchantPaymentData, GetFeesUseCase.Params>(){

    @Suppress("UNCHECKED_CAST")
    override suspend fun execute(params: Params): MerchantPaymentData {
        val response = api.getFeesMerchantPayment(params.merchantCode, params.amount).message


        val serviceCharges = response?.getList("serviceCharges")?.firstOrNull()
        val taxes = serviceCharges?.getString("amount")?.toDoubleOrNull()

/*        val receiverName = response?.getObject("receiver")
            ?.getString("parentName")*/

        val receiverName = response?.getObject("receiver")
            ?.getString("userName") +" "+response?.getObject("receiver")
            ?.getString("lastName")

            val total=taxes!!+ params.amount.toDouble()


       return MerchantPaymentData( params.merchantCode ,params.amount,receiverName.toString(),taxes.toString(),total.toString())
    }

    class Params(
        val merchantCode:String,
        val amount: String,

        )


    private inline fun <reified T> Map<String, Any?>.getValue(key: String): T? = this[key] as? T?

    private fun Map<String, Any?>.getObject(key: String): Map<String, Any?>? = getValue(key)
    private fun Map<String, Any?>.getList(key: String): List<Map<String, Any?>>? = getValue(key)
    private fun Map<String, Any?>.getString(key: String): String? = getValue(key)
}