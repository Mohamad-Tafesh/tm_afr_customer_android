package com.tedmob.afrimoney.features.bills.nawec.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.api.dto.CustomerInvoiceDTO
import com.tedmob.afrimoney.exception.AppException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class GetCustomerInvoicesUseCase
@Inject constructor(
    private val api: TedmobApis,
) : SuspendableUseCase<CustomerInvoiceDTO, String>() {

    override suspend fun execute(params: String): CustomerInvoiceDTO {
        return withContext(Dispatchers.IO) {
            val response = api.getCustomerInvoices(params)

            if (response.tXNSTATUS == "200") {
                response
            } else {
                if (!response.mESSAGE.isNullOrEmpty()) {
                    throw AppException(
                        response.tXNSTATUS?.toIntOrNull() ?: 0,
                        response.mESSAGE.orEmpty(),
                        response.mESSAGE,
                    )
                } else {
                    throw AppException(
                        response.tXNSTATUS?.toIntOrNull() ?: 0,
                        "unexpected error",
                        "unexpected error",
                    )
                }
            }


        }
    }
}