package com.tedmob.afrimoney.data.api

import com.tedmob.afrimoney.data.api.dto.AfrimoneyError
import com.tedmob.afrimoney.exception.AppException

fun throwIfInvalid(txnStatus: String?, errors: List<AfrimoneyError>?) {
    if (!txnStatus.equals("SUCCEEDED", true)) {
        val message = errors?.firstOrNull()?.message
        throw AppException(
            AppException.Code.UNEXPECTED,
            message.orEmpty(),
            "Invalid financial api",
        )
    }
}