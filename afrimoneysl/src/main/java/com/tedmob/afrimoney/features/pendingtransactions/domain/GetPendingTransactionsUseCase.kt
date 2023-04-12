package com.tedmob.afrimoney.features.pendingtransactions.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.api.dto.PendingTransactionsItemDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetPendingTransactionsUseCase
@Inject constructor(
    private val api: TedmobApis,
) : SuspendableUseCase<List<PendingTransactionsItemDTO>, GetPendingTransactionsUseCase.Params>() {

    override suspend fun execute(params: Params): List<PendingTransactionsItemDTO> {
        return withContext(Dispatchers.IO) {
            api.pendingTransactions(params.msisdn, params.pin, params.service).list
        }
    }

    data class Params(val msisdn: String, val pin: String, val service: String)
}


