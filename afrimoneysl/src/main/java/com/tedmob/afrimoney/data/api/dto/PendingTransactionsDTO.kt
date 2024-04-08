package com.tedmob.afrimoney.data.api.dto

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class PendingTransactionsDTO(
    @field:[Expose SerializedName("COMMAND")]
    val command: COMMAND?
) : Parcelable {
    @Parcelize
    data class COMMAND(
        @field:[Expose SerializedName("MESSAGE")]
        val mESSAGE: MESSAGE?,
        @field:[Expose SerializedName("NOOFTXN")]
        val nOOFTXN: String?,
        @field:[Expose SerializedName("TRID")]
        val tRID: String?,
        @field:[Expose SerializedName("TXNID")]
        val tXNID: String?,
        @field:[Expose SerializedName("TXNSTATUS")]
        val tXNSTATUS: String?,
        @field:[Expose SerializedName("TYPE")]
        val tYPE: String?
    ) : Parcelable {
        @Parcelize
        data class MESSAGE(
            @field:[Expose SerializedName("DATA")]
            val dATA: DATA?
        ) : Parcelable {
            @Parcelize
            data class DATA(
                @field:[Expose SerializedName("FIRSTNAME")]
                val fIRSTNAME: List<String>?,
                @field:[Expose SerializedName("FROM")]
                val fROM: List<String>?,
                @field:[Expose SerializedName("LASTNAME")]
                val lASTNAME: List<String>?,
                @field:[Expose SerializedName("PAYID")]
                val pAYID: List<String>?,
                @field:[Expose SerializedName("SERVICENAME")]
                val sERVICENAME: List<String>?,
                @field:[Expose SerializedName("SERVICEREQUESTID")]
                val sERVICEREQUESTID: List<String>?,
                @field:[Expose SerializedName("SERVICETYPE")]
                val sERVICETYPE: List<String>?,
                @field:[Expose SerializedName("TO")]
                val tO: List<String>?,
                @field:[Expose SerializedName("TXNAMT")]
                val tXNAMT: List<String>?,
                @field:[Expose SerializedName("TXNDT")]
                val tXNDT: List<String>?,
                @field:[Expose SerializedName("TXNID")]
                val tXNID: List<String>?
            ) : Parcelable
        }
    }
}
