package com.tedmob.afrimoney.features.pendingtransactions.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.api.dto.PendingTransactionsData
import com.tedmob.afrimoney.exception.AppException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetPendingTransactionsUseCase
@Inject constructor(
    private val api: TedmobApis,
) : SuspendableUseCase<List<PendingTransactionsData>, GetPendingTransactionsUseCase.Params>() {

    override suspend fun execute(params: Params): List<PendingTransactionsData> {
        return withContext(Dispatchers.IO) {
            buildList {

                var data = api.pendingTransactions(params.pin, params.service).command

                if (data?.getString("TXNSTATUS") == "200") {

                    data = data.getObject("MESSAGE")?.getObject("DATA")

                    val count = data?.getObjectOrList("TXNDT")?.size ?: 0


                    val txnDatesList = data?.getObjectOrList("TXNDT")
                    val serviceIdsist = data?.getObjectOrList("SERVICEREQUESTID")
                    val payIdsList = data?.getObjectOrList("PAYID")
                    val lastNameList = data?.getObjectOrList("LASTNAME")
                    val firstNameList = data?.getObjectOrList("FIRSTNAME")
                    val fromList = data?.getObjectOrList("FROM")
                    val toList = data?.getObjectOrList("TO")
                    val txnIdsList = data?.getObjectOrList("TXNID")
                    val txnAmountList = data?.getObjectOrList("TXNAMT")
                    val serviceTypeList = data?.getObjectOrList("SERVICETYPE")
                    val serviceNameList = data?.getObjectOrList("SERVICENAME")

                    for (i in 0 until count) {
                        add(
                            PendingTransactionsData(
                                txnDatesList?.get(i).orEmpty(),
                                serviceIdsist?.get(i).orEmpty(),
                                payIdsList?.get(i).orEmpty(),
                                lastNameList?.get(i).orEmpty(),
                                firstNameList?.get(i).orEmpty(),
                                fromList?.get(i).orEmpty(),
                                toList?.get(i).orEmpty(),
                                txnIdsList?.get(i).orEmpty(),
                                txnAmountList?.get(i).orEmpty(),
                                serviceTypeList?.get(i).orEmpty(),
                                serviceNameList?.get(i).orEmpty(),

                                )
                        )
                    }

                } else {
                    if (!data?.getString("MESSAGE").isNullOrEmpty()) {
                        throw AppException(
                            data?.getString("TXNSTATUS")?.toIntOrNull() ?: 0,
                            data?.getString("MESSAGE").orEmpty(),
                            data?.getString("MESSAGE"),
                        )
                    } else {
                        throw AppException(
                            data?.getString("TXNSTATUS")?.toIntOrNull() ?: 0,
                            "unexpected error",
                            "unexpected error",
                        )
                    }
                }


            }
        }
    }

    data class Params(val pin: String, val service: String)


    private inline fun <reified T> Map<String, Any?>.getValue(key: String): T? = this[key] as? T?

    private fun Map<String, Any?>.getObject(key: String): Map<String, Any?>? = getValue(key)
    private fun Map<String, Any?>.getList(key: String): List<String>? = getValue(key)
    private fun Map<String, Any?>.getString(key: String): String? = getValue(key)

    private fun Map<String, Any?>.getObjectOrList(key: String): List<String>? {
        val value = this[key]
        return when (value) {
            is String -> listOf(value.toString())
            is List<*> -> {
                if (value.all { it is String }) {
                    value as List<String>
                } else {
                    null
                }
            }
            else -> null
        }
    }

}


