package com.tedmob.afrimoney.features.home.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.entity.UserHomeData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetHomeDataUseCase
@Inject constructor(
    private val api: TedmobApis,
) : SuspendableUseCase<UserHomeData, Unit>() {

    override suspend fun execute(params: Unit): UserHomeData {
        return withContext(Dispatchers.IO) {
            //TODO api call
            UserHomeData(
                5,
                5,
                2,
            )
        }
    }
}