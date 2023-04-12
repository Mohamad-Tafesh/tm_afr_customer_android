package com.tedmob.afrimoney.features.launch.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RefreshLoginUseCase
@Inject constructor(
    private val api: TedmobApis,
): SuspendableUseCase<Unit, Unit>(){

    override suspend fun execute(params: Unit) {
        return withContext(Dispatchers.IO) {
            api.fetchToken()
        }
    }
}