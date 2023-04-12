package com.tedmob.afrimoney.features.bills.waec.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.api.dto.FeesWaecDTO
import com.tedmob.afrimoney.data.entity.GetFeesData
import javax.inject.Inject

class GetFeesCheckWaecUseCase
@Inject constructor(
    private val api: TedmobApis,
) : SuspendableUseCase<String, String>(){

    override suspend fun execute(params: String): String {
        val response=api.feesWaec()
        var index: Int = -1
        var id: String?= null
        if (params=="1") id="2" else id="1"
        index = response.WAECFeeList?.indexOfFirst { it.FeeId == id }?:-1
        return response.WAECFeeList?.get(index)?.FeeAmount.toString()
    }


}