package com.tedmob.afrimoney.features.africellservices.airtime.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.entity.SubmitResult
import javax.inject.Inject

class AirtimeUseCase
@Inject constructor(

    private val api: TedmobApis,
) : SuspendableUseCase<SubmitResult, AirtimeUseCase.Params>() {

    override suspend fun execute(params: Params): SubmitResult {
        //TODO
        val response = api.transferMoney(params.number, params.amount, params.pin)
        lateinit var message: String
        if (!response.status.equals("SUCCEEDED", true)) {
            if (response.errors.isNullOrEmpty()) {
                message = response.message.toString()
            } else {
                message = response.errors.firstOrNull()?.message.toString()
            }

            return SubmitResult.Failure(message)
        } else return SubmitResult.Success(
            response.message.toString()
        )
    }

    class Params(
        val number: String,
        val amount: String,
        val pin: String,


        )

}