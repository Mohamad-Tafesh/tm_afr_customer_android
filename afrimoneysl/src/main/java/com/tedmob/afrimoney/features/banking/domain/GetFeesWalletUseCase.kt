package com.tedmob.afrimoney.features.banking.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.entity.GetFeesData
import javax.inject.Inject

class GetFeesWalletUseCase
@Inject constructor(
    private val api: TedmobApis,
) : SuspendableUseCase<GetFeesData, GetFeesWalletUseCase.Params>(){

    @Suppress("UNCHECKED_CAST")
    override suspend fun execute(params: Params): GetFeesData {
        val data = api.getFeesWalletToBank(params.bankId,params.bankNumber, params.amount).message

        val serviceCharges = data?.getList("serviceCharges")?.firstOrNull()
        //val taxes1 = serviceCharges?.getString("amount")?.toDoubleOrNull()
     // val taxes2 = serviceCharges?.getList("taxes")?.sumOf { it.getString("amount")?.toDoubleOrNull() ?: 0.0 }

        val receiverName = data?.getObject("receiver")
            ?.getString("parentName")

        val total=params.amount.toDouble()
       return GetFeesData( params.bankNumber ,params.amount, total.toString(),receiverName!!,total.toString())
    }

    class Params(
        val bankId:String,
        val bankNumber:String,
        val amount: String,

    )


    private inline fun <reified T> Map<String, Any?>.getValue(key: String): T? = this[key] as? T?

    private fun Map<String, Any?>.getObject(key: String): Map<String, Any?>? = getValue(key)
    private fun Map<String, Any?>.getList(key: String): List<Map<String, Any?>>? = getValue(key)
    private fun Map<String, Any?>.getString(key: String): String? = getValue(key)
}