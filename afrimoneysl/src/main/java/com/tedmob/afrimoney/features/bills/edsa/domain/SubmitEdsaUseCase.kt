package com.tedmob.afrimoney.features.bills.edsa.domain

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

class SubmitEdsaUseCase
@Inject constructor(

    private val api: TedmobApis,
) : SuspendableUseCase<SubmitResult, SubmitEdsaUseCase.Params>() {

    override suspend fun execute(params: Params): SubmitResult {


        val response = api.edsa(params.nb, params.amount, params.pin)
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
        val nb: String,
        val amount: String,
        val pin: String,


        )

}