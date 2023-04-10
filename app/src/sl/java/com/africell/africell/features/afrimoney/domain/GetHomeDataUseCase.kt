package com.africell.africell.features.afrimoney.domain

import com.africell.africell.app.usecase.SuspendableUseCase
import com.africell.africell.data.api.RestApi
import com.africell.africell.data.entity.afrimoney.HomeData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetHomeDataUseCase
@Inject constructor(
    private val api: RestApi,
) : SuspendableUseCase<HomeData, Unit>() {

    override suspend fun execute(params: Unit): HomeData {
        return withContext(Dispatchers.IO) {
            //TODO api call
            HomeData(
                "1,000",
                "1,500",
                0,
            )
        }
    }
}