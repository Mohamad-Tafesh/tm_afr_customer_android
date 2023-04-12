package com.tedmob.afrimoney.features.launch.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.api.dto.AppSettingsDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetAppSettingsUseCase
@Inject constructor(
    private val api: TedmobApis,
) : SuspendableUseCase<AppSettingsDTO, Unit>() {

    override suspend fun execute(params: Unit): AppSettingsDTO {
        //return api.appSettings()
        return withContext(Dispatchers.IO) {
            delay(500L)

            AppSettingsDTO(
                false,
                "1.0.0",//10000
                "Force Update",
                "Please update the app"
            )
            /*AppSettingsDTO(
                true,
                "1.0.1",//10001
                "Force Update",
                "Please update the app"
            )*/
        }//TODO replace with api call
    }
}