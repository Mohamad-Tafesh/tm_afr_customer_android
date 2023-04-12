package com.tedmob.afrimoney.features.contactus.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetContactUsContentUseCase
@Inject constructor(
    private val api: TedmobApis,
) : SuspendableUseCase<Any, Unit>() {
    override suspend fun execute(params: Unit): Any {
        return withContext(Dispatchers.IO) { //TODO api call
            Any()
        }
    }
}