package com.tedmob.afrimoney.features.africellservices.airtime.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.entity.SubmitResult
import javax.inject.Inject

class AirtimeSelfConfirmationUseCase
@Inject constructor(

    private val api: TedmobApis,
) : SuspendableUseCase<SubmitResult, AirtimeSelfConfirmationUseCase.Params>() {

    override suspend fun execute(params: Params): SubmitResult {

        val response = api.bundleSelf(
            params.remark,
            params.transactionAmount,
            params.pin,
            params.idValue,
            params.idType,
            params.bundle,
            params.walletID,
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
        val remark: String,
        val transactionAmount: String,
        val pin: String,
        val idValue: String,
        val idType: String,
        val bundle: String,
        val walletID: String,

        )

}