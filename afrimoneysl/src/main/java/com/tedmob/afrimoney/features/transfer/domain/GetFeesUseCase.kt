package com.tedmob.afrimoney.features.transfer.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.entity.GetFeesData
import com.tedmob.afrimoney.data.repository.PrefSessionRepository
import javax.inject.Inject

class GetFeesUseCase
@Inject constructor(
    private val api: TedmobApis,
    private val sessionRepository: PrefSessionRepository,
) : SuspendableUseCase<GetFeesData, GetFeesUseCase.Params>(){

    @Suppress("UNCHECKED_CAST")
    override suspend fun execute(params: Params): GetFeesData {

        val isAfrimoneyUser = api.checkIfAfrimoneyUser(params.number)

        val data = api.getFeesCashOut(params.number, params.amount,isAfrimoneyUser).message

        val serviceCharges = data?.getList("serviceCharges")?.firstOrNull()
        val taxes1 = serviceCharges?.getString("amount")?.toDoubleOrNull()?: 0.0
        val taxes2 = serviceCharges?.getList("taxes")
            ?.sumOf { it.getString("amount")?.toDoubleOrNull() ?: 0.0 }

        var receiverName = data?.getObject("receiver")?.getString("parentName")
        if (receiverName == null) receiverName =
           ( data?.getObject("receiver")?.getString("userName")?:"") + " " + (data?.getObject("receiver")?.getString("lastName")?:"")

        val total = taxes1 + (taxes2 ?: 0.0) + params.amount.toDouble()
       return GetFeesData( params.number ,params.amount,(taxes1 + (taxes2 ?:0.0)).toString(),receiverName,total.toString())
    }

    class Params(
        val number:String,
        val amount: String,

    )


    private inline fun <reified T> Map<String, Any?>.getValue(key: String): T? = this[key] as? T?

    private fun Map<String, Any?>.getObject(key: String): Map<String, Any?>? = getValue(key)
    private fun Map<String, Any?>.getList(key: String): List<Map<String, Any?>>? = getValue(key)
    private fun Map<String, Any?>.getString(key: String): String? = getValue(key)
}