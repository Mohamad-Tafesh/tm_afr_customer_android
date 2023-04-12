package com.tedmob.afrimoney.features.account.domain

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

class ChangePinUseCase
@Inject constructor(

    private val api: TedmobApis,
) : SuspendableUseCase<SubmitResult, ChangePinUseCase.Params>() {

    override suspend fun execute(params: Params): SubmitResult {


        val response = api.changePin(params.oldPin, params.newPin)

        if (!response.status.equals("200", true)) {
            return SubmitResult.Failure(response.message!!)
        } else return SubmitResult.Success(
            response.message!!
        )

    }

    class Params(
        val oldPin: String,
        val newPin: String,


        )

}