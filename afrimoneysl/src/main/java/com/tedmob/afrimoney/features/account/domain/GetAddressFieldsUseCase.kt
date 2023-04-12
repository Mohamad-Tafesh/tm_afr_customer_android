package com.tedmob.afrimoney.features.account.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.entity.ComboEntry
import com.tedmob.afrimoney.data.entity.MyAddressFields
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetAddressFieldsUseCase
@Inject constructor(
    private val api: TedmobApis,
) : SuspendableUseCase<MyAddressFields, Unit>() {

    override suspend fun execute(params: Unit): MyAddressFields {
        return withContext(Dispatchers.IO) {
            MyAddressFields(
                listOf(
                    ComboEntry("1", "Providence"),
                ),
            )
        }
    }
}