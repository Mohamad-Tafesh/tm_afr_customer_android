package com.tedmob.afrimoney.features.bills.covid.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.entity.Press
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetAmountTravelUseCase
@Inject constructor(
    private val api: TedmobApis,
) : SuspendableUseCase<String, Unit>() {

    override suspend fun execute(params: Unit):String {
        return withContext(Dispatchers.IO) {
            val response = api.getDSTVPress().data
            ""
        }
    }



}
