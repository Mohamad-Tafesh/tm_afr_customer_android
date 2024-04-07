package com.tedmob.afrimoney.data.api.dto

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tedmob.afrimoney.ui.spinner.searchable.MaterialSearchableSpinnerItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClientDetails(
    @SerializedName("COMPANYCODE")
    @Expose val cOMPANYCODE: String?,
    @SerializedName("CONSUMERACCOUNTNUMBER")
    @Expose val cONSUMERACCOUNTNUMBER: String?,
    @SerializedName("NICKNAME")
    @Expose val clientName: String?,
    @SerializedName("PREF1NAME")
    @Expose val PREF1NAME: String?,
    @SerializedName("PREF1VALUE")
    @Expose val clientId: String?
) : MaterialSearchableSpinnerItem(), Parcelable {

    override fun toString(): String =
        clientName.orEmpty() + "\n" + clientId //also used for spinner's selected view

    override fun toDisplayString(): String =
        clientName.orEmpty() + "\n" + clientId //used for spinner's searchable dialog's items
}
