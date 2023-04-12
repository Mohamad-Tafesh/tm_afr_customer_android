package com.tedmob.afrimoney.features.authentication.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import javax.inject.Inject

class SetPinUseCase
@Inject constructor(
    //...
) : SuspendableUseCase<Boolean, String>() {

    override suspend fun execute(params: String): Boolean {
        return true //TODO api call
    }
}