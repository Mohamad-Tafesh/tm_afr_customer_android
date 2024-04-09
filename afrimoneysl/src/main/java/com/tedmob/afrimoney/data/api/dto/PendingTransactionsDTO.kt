package com.tedmob.afrimoney.data.api.dto

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


class PendingTransactionsDTO(
    @field:[Expose SerializedName("COMMAND")] val command: Map<String, Any?>?
)