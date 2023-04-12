package com.tedmob.afrimoney.features.bills.waec.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.entity.GetFeesData
import javax.inject.Inject

class GetFeesRegisterWaecUseCase
@Inject constructor(
    private val api: TedmobApis,
) : SuspendableUseCase<String, Unit>(){

    override suspend fun execute(params: Unit): String {
       //TODO call api\
        return ""
    }


}