package com.tedmob.afrimoney.features.transfer.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.entity.SubmitResult
import com.tedmob.afrimoney.exception.AppException
import com.tedmob.afrimoney.features.authentication.domain.VerifyPinUseCase
import com.tedmob.afrimoney.util.number.defaultCurrency
import com.tedmob.afrimoney.util.number.formatAmountWith
import kotlinx.coroutines.delay
import java.math.BigDecimal
import javax.inject.Inject

class SubmitTransferMoneyDataUseCase
@Inject constructor(

    private val api: TedmobApis,
) : SuspendableUseCase<SubmitResult, SubmitTransferMoneyDataUseCase.Params>() {

    override suspend fun execute(params: Params): SubmitResult {

        params.number?.let {
            val isAfrimoneyUser = api.checkIfAfrimoneyUser(it)

            val response = api.transferMoney(it, params.amount, params.pin,isAfrimoneyUser)
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
        }?:run {
            val response = api.transferMoneyFromRemittance(params.amount, params.pin)
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



    }

    class Params(
        val number: String?,
        val amount: String,
        val pin: String,
        )

}