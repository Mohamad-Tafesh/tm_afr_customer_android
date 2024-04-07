package com.tedmob.afrimoney.features.bills.nawec.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.entity.SubmitResult
import com.tedmob.afrimoney.data.repository.domain.SessionRepository
import javax.inject.Inject

class SubmitNawecUseCase
@Inject constructor(

    private val api: TedmobApis,
    private val session: SessionRepository,
) : SuspendableUseCase<SubmitResult, SubmitNawecUseCase.Params>() {

    override suspend fun execute(params: Params): SubmitResult {
        val response = api.confirmBuyNawec(
            params.number,
            params.amount,
            params.pin,
            params.uniqueNumber,
            params.dateNTime
        )
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
        val uniqueNumber: String,
        val dateNTime: String,
    )

}