package com.tedmob.afrimoney.features.pendingtransactions.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.api.dto.ConfirmTransferMoneyDTO
import com.tedmob.afrimoney.data.entity.SubmitResult
import javax.inject.Inject

class SubmitPendingTransactionUseCase
@Inject constructor(

    private val api: TedmobApis,
) : SuspendableUseCase<SubmitResult, SubmitPendingTransactionUseCase.Params>() {

    override suspend fun execute(params: Params): SubmitResult {
        lateinit var response: ConfirmTransferMoneyDTO
        if (params.type.equals("c")) {
            response = api.confirmCashOutPendingTransaction(params.pin, params.svId)
        } else response = api.confirmMerchantPendingTransaction(params.pin, params.svId)

        lateinit var message: String
        if (!response.status.equals("SUCCEEDED", true)) {
            if (response.errors.isNullOrEmpty()) {
                message = response.message.toString()
            } else {
                message = response.errors!!.firstOrNull()?.message.toString()
            }

            return SubmitResult.Failure(message)
        } else return SubmitResult.Success(
            response.message.toString()
        )
    }

    class Params(
        val type: String,
        val pin: String,
        val svId: String,

        )

}