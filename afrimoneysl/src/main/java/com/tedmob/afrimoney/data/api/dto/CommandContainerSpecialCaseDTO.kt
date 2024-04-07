package com.tedmob.afrimoney.data.api.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tedmob.afrimoney.exception.AppException

class CommandContainerSpecialCaseDTO<T : CommandContainerSpecialCaseDTO.Item2>(
    @field:[Expose SerializedName("COMMAND")] val command: T,
) {
     open class Item2(
        @field:[Expose SerializedName("BILLER")]
        var biller: Item? = null
    )

    open class Item {
        @field:[Expose SerializedName("TXNSTATUS")]
        var status: String? = null

        @field:[Expose SerializedName("MESSAGE")]
        var message: String? = null

    }


    suspend fun getCommandOrThrow(): T {
        return command
/*        return if (command.biller?.status == "200") {
            command
        } else {
            command
            CommandContainerSpecialCaseDTO.Item2(
                command.biller?.status,
                command.biller?.message.orEmpty(),
            )
        }*/

    }
}
