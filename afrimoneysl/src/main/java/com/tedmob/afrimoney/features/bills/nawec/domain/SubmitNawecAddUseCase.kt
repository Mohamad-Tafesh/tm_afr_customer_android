package com.tedmob.afrimoney.features.bills.nawec.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.api.dto.AddClientDTO
import javax.inject.Inject

class SubmitNawecAddUseCase
@Inject constructor(

    private val api: TedmobApis,
) : SuspendableUseCase<AddClientDTO, SubmitNawecAddUseCase.Params>() {

    override suspend fun execute(params: Params): AddClientDTO {

      return  api.nawecAddClient(params.clientId, params.nickname, params.pin)

    }

    class Params(
        val clientId: String,
        val nickname: String,
        val pin: String,


        )

}