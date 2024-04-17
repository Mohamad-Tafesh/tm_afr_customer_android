package com.tedmob.afrimoney.features.banking.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.entity.GetFeesData
import com.tedmob.afrimoney.data.entity.SubmitResult
import javax.inject.Inject

class GetBankBalanceEnquiryUseCase
@Inject constructor(
    private val api: TedmobApis,
) : SuspendableUseCase<SubmitResult, GetBankBalanceEnquiryUseCase.Params>(){

    @Suppress("UNCHECKED_CAST")
    override suspend fun execute(params: Params): SubmitResult {
        val response = api.getBalanceEnquiry(params.bankId,params.accNb,params.pin)
        if (response.commad.status!="200"){
            return SubmitResult.Failure(response.commad.message.toString())
        }else return SubmitResult.Success(
            response.commad.message.toString()
        )

    }

    class Params(
        val bankId:String,
        val accNb:String,
        val pin: String,

    )


    private inline fun <reified T> Map<String, Any?>.getValue(key: String): T? = this[key] as? T?

    private fun Map<String, Any?>.getObject(key: String): Map<String, Any?>? = getValue(key)
    private fun Map<String, Any?>.getList(key: String): List<Map<String, Any?>>? = getValue(key)
    private fun Map<String, Any?>.getString(key: String): String? = getValue(key)
}