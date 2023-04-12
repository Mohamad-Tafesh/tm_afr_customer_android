package com.tedmob.afrimoney.features.account.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.api.dto.LastTransactionResponseDTO
import com.tedmob.afrimoney.data.entity.LastTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetLastTransactionsUseCase
@Inject constructor(
    private val api: TedmobApis,
) : SuspendableUseCase<List<LastTransaction>, GetLastTransactionsUseCase.Params>() {

    override suspend fun execute(params: Params): List<LastTransaction> {
        return withContext(Dispatchers.IO) {
            val response = api.getLastNTransactions(params.number,params.pin).data?.transactionData

            if (response != null) {
                buildList {
                    var index = 0
                    while (response.hasElementAt(index)) {
                        add(
                            LastTransaction(
                                response.attr1Value?.getOrNull(index),
                                response.payId?.getOrNull(index),
                                response.firstName?.getOrNull(index),
                                response.from?.getOrNull(index),
                                response.txnType?.getOrNull(index),
                                response.txnId?.getOrNull(index),
                                response.txnAmt?.getOrNull(index),
                                response.txnDt?.getOrNull(index),
                                response.attr3Value?.getOrNull(index),
                                response.lastName?.getOrNull(index),
                                response.attr2Value?.getOrNull(index),
                                response.service?.getOrNull(index),
                                response.txnStatus?.getOrNull(index),
                            )
                        )
                        index++
                    }
                }
            } else {
                emptyList()
            }
        }
    }

    data class Params(val number:String,val pin:String)

    private fun LastTransactionResponseDTO.TransactionData.hasElementAt(index: Int) =
        attr1Value?.elementAtOrNull(index) != null ||
                payId?.elementAtOrNull(index) != null ||
                firstName?.elementAtOrNull(index) != null ||
                from?.elementAtOrNull(index) != null ||
                txnType?.elementAtOrNull(index) != null ||
                txnId?.elementAtOrNull(index) != null ||
                txnAmt?.elementAtOrNull(index) != null ||
                txnDt?.elementAtOrNull(index) != null ||
                attr3Value?.elementAtOrNull(index) != null ||
                lastName?.elementAtOrNull(index) != null ||
                attr2Value?.elementAtOrNull(index) != null ||
                service?.elementAtOrNull(index) != null ||
                txnStatus?.elementAtOrNull(index) != null
}