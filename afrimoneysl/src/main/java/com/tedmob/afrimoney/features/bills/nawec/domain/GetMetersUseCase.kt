package com.tedmob.afrimoney.features.bills.nawec.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.api.dto.ClientDetails
import com.tedmob.afrimoney.data.api.dto.ListOrObject
import javax.inject.Inject

class GetMetersUseCase
@Inject constructor(
    private val api: TedmobApis,
) : SuspendableUseCase<ListOrObject<ClientDetails>, Unit>() {

    @Suppress("UNCHECKED_CAST")
    override suspend fun execute(params: Unit): ListOrObject<ClientDetails> {
        val data = api.getMeters()
        if (data.commad.status=="200"){
            return data.commad.clientDetails?: ListOrObject(emptyList())
        }
        return ListOrObject(emptyList())

    }

}