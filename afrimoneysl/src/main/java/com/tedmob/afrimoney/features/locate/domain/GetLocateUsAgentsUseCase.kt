package com.tedmob.afrimoney.features.locate.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.AfrimoneyRestApi
import com.tedmob.afrimoney.data.entity.LocateUsEntry
import com.tedmob.modules.mapcontainer.view.MapLatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Credentials
import javax.inject.Inject

class GetLocateUsAgentsUseCase
@Inject constructor(
    private val api: AfrimoneyRestApi,
) : SuspendableUseCase<List<LocateUsEntry>, Params>() {

    override suspend fun execute(params: Params): List<LocateUsEntry> {
        return withContext(Dispatchers.IO) {
            val response = api.getShopLocation(credentials)
            buildList {
                response.forEach {
                    add(
                        LocateUsEntry(
                            MapLatLng(it.latitude!!.toDouble(), it.longitude!!.toDouble()),
                            it.displayDistance(params.lat, params.long),
                            it.shopName,
                            it.address,
                            it.telephoneNumber.orEmpty(),
                        )
                    )
                }
            }

        }
    }
}

val credentials: String = Credentials.basic("sc-afr-sl-api", "s@c_2hg!0m9k")

data class Params(val lat: Double?, val long: Double?)