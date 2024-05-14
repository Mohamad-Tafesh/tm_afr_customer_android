package com.tedmob.afrimoney.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tedmob.afrimoney.exception.AppException

class CommandContainerDTO<T : CommandContainerDTO.Item>(
    @field:[Expose SerializedName("COMMAND")] val command: T,
) {
    open class Item {
        @field:[Expose SerializedName("TXNSTATUS")]
        var status: String? = null

        @field:[Expose SerializedName("MESSAGE")]
        var message: String? = null
    }


    suspend fun getCommandOrThrow(): T {
        return if (command.status == "200") {
            command
        } else {
            if (!command.message.isNullOrEmpty()) {
                throw AppException(
                    command.status?.toIntOrNull() ?: 0,
                    command.message.orEmpty(),
                    command.message,
                )
            } else {
                throw AppException(
                    command.status?.toIntOrNull() ?: 0,
                    "unexpected error",
                    "unexpected error",
                )
            }
        }
    }

    suspend fun getStatus(): Boolean {
        return if (command.status == "200") {
            true
        } else {
            false
        }
    }
}
