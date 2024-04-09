package com.tedmob.afrimoney.features.bills.nawec.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.exception.AppException
import com.tedmob.afrimoney.features.bills.nawec.GetNawecFeesData
import javax.inject.Inject

class GetFeesNawecUseCase
@Inject constructor(
    private val api: TedmobApis,
) : SuspendableUseCase<GetNawecFeesData, GetFeesNawecUseCase.Params>() {

    override suspend fun execute(params: Params): GetNawecFeesData {
        val data = api.getFeesNawec(params.meterNumber, params.amount)

        if (!data.tXNSTATUS.equals("200", true)) {
            val message = data.mESSAGE
            throw AppException(
                AppException.Code.UNEXPECTED,
                message.orEmpty(),
                "Invalid financial api",
            )
        } else {

            return GetNawecFeesData(
                params.meterNumber,
                params.amount,
                params.name,
         /*       data.serCharge.orEmpty(),
                data.tariffName,
                data.unitValue.orEmpty(),
                data.siUnit.orEmpty(),
                data.uniqueNumber.orEmpty(),
                data.dateNTime.orEmpty()*/
            )
        }
    }

    class Params(
        val meterNumber: String,
        val amount: String,
        val name: String,
    )

}