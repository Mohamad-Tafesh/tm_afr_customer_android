package com.tedmob.afrimoney.features.banking.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.entity.Bank
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetBanksUseCase
@Inject constructor(
    private val api: TedmobApis,
) : SuspendableUseCase<List<Bank>, Unit>() {

    override suspend fun execute(params: Unit): List<Bank> {
        return withContext(Dispatchers.IO) {

            val AvailBanks = api.getAvailBank().list
                ?.map {
                    Bank(
                        "",
                        it.name.orEmpty(),
                        it.id.orEmpty(),
                        "",
                        false
                    )

                }
                .orEmpty()
                .toMutableList()
            val banks2 = run {
                var response = api.getBankAccounts().data
                val list_object = response?.get("ACCOUNT")
                if (list_object is List<*>) {


                    val size = response?.getList("ACCOUNT")?.size ?: 0
                    buildList {
                        for (index in 0 until size) {
                            var id: String =
                                response?.getList("ACCOUNT")?.get(index)?.get("BANKID") as String
                            AvailBanks.removeAll { it.bankid == id }
                            add(
                                Bank(
                                    response.getList("ACCOUNT")?.get(index)
                                        ?.get("PRIMARY") as String,
                                    response.getList("ACCOUNT")?.get(index)?.get("BNAME") as String,
                                    response.getList("ACCOUNT")?.get(index)
                                        ?.get("BANKID") as String,
                                    response.getList("ACCOUNT")?.get(index)
                                        ?.get("ACCNUM") as String,
                                    true

                                )

                            )

                        }
                    }

                } else {
                    if (response?.getObject("ACCOUNT") != null) {
                        buildList {
                            add(
                                Bank(
                                    response.getObject("ACCOUNT")?.get("PRIMARY") as String,
                                    response.getObject("ACCOUNT")?.get("BNAME") as String,
                                    response.getObject("ACCOUNT")?.get("BANKID") as String,
                                    response.getObject("ACCOUNT")?.get("ACCNUM") as String,
                                    true

                                )

                            )

                        }
                    } else emptyList()

                }


            }

            val result = AvailBanks + banks2
            result

            /*   val response = api.getBankAccounts().data
               val size = response?.getList("ACCOUNT")?.size ?: 0
               var list: List<Bank> = buildList {
                   add(
                       Bank(
                           "",
                           "SLCB",
                           "",
                           "",
                           false

                       )
                   )
                   add(
                       Bank(
                           "",
                           "ACCESS BANK",
                           "",
                           "",
                           false

                       )
                   )
                   add(
                       Bank(
                           "",
                           "ECOBANK",
                           "",
                           "",
                           false

                       )
                   )


                   for (index in 0 until size) {
                       var name: String =
                           response?.getList("ACCOUNT")?.get(index)?.get("BNAME") as String
                       if (name.equals("SLCB") || name.equals("ACCESS BANK") || name.equals("ECOBANK")) {
                           removeAll { it.bname == name }
                       }
                       add(
                           Bank(
                               response.getList("ACCOUNT")?.get(index)?.get("PRIMARY") as String,
                               response.getList("ACCOUNT")?.get(index)?.get("BNAME") as String,
                               response.getList("ACCOUNT")?.get(index)?.get("BANKID") as String,
                               response.getList("ACCOUNT")?.get(index)?.get("ACCNUM") as String,
                               true

                           )

                       )

                   }


               }
               list*/
        }
    }

    private inline fun <reified T> Map<String, Any?>.getValue(key: String): T? = this[key] as? T?
    private fun Map<String, Any?>.getList(key: String): List<Map<String, Any?>>? = getValue(key)
    private fun Map<String, Any?>.getObject(key: String): Map<String, Any?>? = getValue(key)

}

