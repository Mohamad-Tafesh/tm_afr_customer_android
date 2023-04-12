package com.tedmob.afrimoney.features.bills.waec.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.entity.SubmitResult
import javax.inject.Inject

class SubmitWaecDataUseCase
@Inject constructor(

    private val api: TedmobApis,
) : SuspendableUseCase<SubmitResult, SubmitWaecDataUseCase.Params>() {

    override suspend fun execute(params: Params): SubmitResult {

        val response = api.Waec(params.pin, params.amount, params.type)
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
        val pin: String,
        val amount: String,
        val type: String


    )

}