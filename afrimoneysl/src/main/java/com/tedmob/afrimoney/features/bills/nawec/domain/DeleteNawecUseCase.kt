package com.tedmob.afrimoney.features.bills.nawec.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.api.dto.AddClientDTO
import com.tedmob.afrimoney.data.entity.SubmitResult
import javax.inject.Inject

class DeleteNawecUseCase
@Inject constructor(

    private val api: TedmobApis,
) : SuspendableUseCase<SubmitResult, DeleteNawecUseCase.Params>() {

    override suspend fun execute(params: Params): SubmitResult {

        val response = api.deleteCustomer(params.accNb,params.nickname, params.pin)
        lateinit var message: String
        if (!response.command?.status.equals("200", true)) {
            if (response.command?.errors.isNullOrEmpty()) {
                message = response.command?.message.toString()
            } else {
                message = response.command?.errors?.firstOrNull()?.message.toString()
            }

            return SubmitResult.Failure(message)
        } else return SubmitResult.Success(
            response.command?.message.toString()
        )




    }
    class Params(
        val accNb: String,
        val nickname: String,
        val pin: String,


        )
}