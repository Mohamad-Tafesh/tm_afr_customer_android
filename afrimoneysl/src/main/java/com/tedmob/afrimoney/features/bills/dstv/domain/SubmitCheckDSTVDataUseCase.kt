package com.tedmob.afrimoney.features.bills.dstv.domain

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

class SubmitCheckDSTVDataUseCase
@Inject constructor(

    private val api: TedmobApis,
) : SuspendableUseCase<SubmitResult, String>() {

    override suspend fun execute(params: String): SubmitResult {


        val response = api.checkDSTV(params)
        if (response.txnStatus == "200") {
            return SubmitResult.Success(
                "Your Amount: NLe" + response.dueAmount + "\nDue Date: " + response.dueDate
            )
        } else {
            return SubmitResult.Failure(
                response.message.toString()
            )
        }


    }


}