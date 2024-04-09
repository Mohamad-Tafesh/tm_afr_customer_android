package com.tedmob.afrimoney.features.bills.nawec.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.api.dto.ClientNawecDTO
import com.tedmob.afrimoney.exception.AppException
import javax.inject.Inject

class GetMeterUseCase
@Inject constructor(

    private val api: TedmobApis,
) : SuspendableUseCase<ClientNawecDTO, String>() {

    override suspend fun execute(params: String): ClientNawecDTO {

    return api.getMeter(params)
    }

      /*  val data = api.getMeter(params)

        if (!data.txnStatus.equals("200", true)) {
            val message = data.message
            throw AppException(
                AppException.Code.UNEXPECTED,
                message.orEmpty(),
                "Invalid Add api",
            )
        } else {

           return data
        }
    }*/


}
