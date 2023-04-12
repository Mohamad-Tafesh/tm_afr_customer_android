package com.tedmob.afrimoney.features.bills.dstv.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.entity.GetFeesData
import javax.inject.Inject

class ProceedRenewUseCase
@Inject constructor(
    private val api: TedmobApis,
) : SuspendableUseCase<String, ProceedRenewUseCase.Params>(){

    @Suppress("UNCHECKED_CAST")
    override suspend fun execute(params: Params): String {
        val data = api.proceedDSTV(params.language, params.type,params.month,params.cardNb)

       return data.amount.toString()
    }

    class Params(
        val language:String,
        val type: String,
        val month: String,
        val cardNb:String

    )


}