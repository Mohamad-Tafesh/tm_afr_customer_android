package com.tedmob.afrimoney.features.locate.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.entity.LocateUsEntry
import com.tedmob.modules.mapcontainer.view.MapLatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetLocateUsStoresUseCase
@Inject constructor(
    private val api: TedmobApis,
): SuspendableUseCase<List<LocateUsEntry>, Unit>(){

    override suspend fun execute(params: Unit): List<LocateUsEntry> {
        return withContext(Dispatchers.IO) { //TODO api call
            listOf(
                LocateUsEntry(
                    MapLatLng(33.9493681,35.5955808),
                    "N/A",
                    "TEDMOB",
                    "Dbayeh",
                    "04545466",
                )
            )
        }
    }
}