package com.tedmob.afrimoney.features.bills.dstv.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.entity.Press
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetPressUseCase
@Inject constructor(
    private val api: TedmobApis,
) : SuspendableUseCase<List<Press>, Unit>() {

    override suspend fun execute(params: Unit): List<Press> {
        return withContext(Dispatchers.IO) {
            val response = api.getDSTVPress().data


            buildList {
                for (index in 0 until response!!.size) {

                    add(
                        Press(
                            response.get(index).get("Name") as String,
                            response.get(index).get("ProductCode") as String,
                            buildList {
                                var size = response.get(index).getList("SubscriptionType")?.size
                                for (index2 in 0 until size!!) {

                                    add(
                                        response.get(index).getList("SubscriptionType")
                                            ?.get(index2)?.get("Type") as String
                                    )

                                }
                            }


                        )
                    )
                }

            }
        }
    }

    private inline fun <reified T> Map<String, Any?>.getValue(key: String): T? = this[key] as? T?
    private fun Map<String, Any?>.getList(key: String): List<Map<String, Any?>>? = getValue(key)


}
