package com.tedmob.afrimoney.data.api

import com.tedmob.afrimoney.data.api.dto.AfrimoneyError
import com.tedmob.afrimoney.exception.AppException

fun throwIfInvalid(txnStatus: String?, error: String?) {
    if (!txnStatus.equals("SUCCEEDED", true)) {
        val message = error
        throw AppException(
            AppException.Code.UNEXPECTED,
            message.orEmpty(),
            "Invalid financial api",
        )
    }
}