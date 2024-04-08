package com.tedmob.afrimoney.features.pendingtransactions.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.api.dto.PendingTransactionsData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetPendingTransactionsUseCase
@Inject constructor(
    private val api: TedmobApis,
) : SuspendableUseCase<List<PendingTransactionsData>, GetPendingTransactionsUseCase.Params>() {

    override suspend fun execute(params: Params): List<PendingTransactionsData> {
        return withContext(Dispatchers.IO) {
            buildList{
                api.pendingTransactions(params.pin, params.service).command?.mESSAGE?.dATA.let {

                    val count = it?.fROM?.size ?: 0
                    for (i in 0 until count) {
                        add(
                            PendingTransactionsData(
                                it?.tXNDT?.get(i).orEmpty().orEmpty(),
                                it?.sERVICEREQUESTID?.get(i).orEmpty(),
                                it?.pAYID?.get(i).orEmpty(),
                                it?.lASTNAME?.get(i).orEmpty(),
                                it?.fIRSTNAME?.get(i).orEmpty(),
                                it?.fROM?.get(i).orEmpty(),
                                it?.tO?.get(i).orEmpty(),
                                it?.tXNID?.get(i).orEmpty(),
                                it?.tXNAMT?.get(i).orEmpty(),
                                it?.sERVICETYPE?.get(i).orEmpty(),
                                it?.sERVICENAME?.get(i).orEmpty(),
                            )
                        )
                    }

                }
            }
        }
    }

    data class Params(val pin: String, val service: String)
}


