package com.tedmob.afrimoney.features.authentication

import android.graphics.Bitmap
import com.tedmob.modules.mapcontainer.view.MapLatLng
import java.util.*

class RegisterStepsData(
    var step1: Step1 = Step1(),
    var step2: Step2 = Step2(),
    var addressCoordinates: MapLatLng? = null,
    var addressDetails: AddressDetails? = null,
) {
    data class Step1(
      //  val image: Bitmap? = null,
        val firstName: String = "",
        val lastName: String = "",
        val gender: String? = null,
        val dob: Calendar? = null,
       // val invitationCode: String? = null,
    )

    data class Step2(
        val idNumber: String = "",
        val idType: String = "",
        val frontImage: Bitmap? = null,
        val backImage: Bitmap? = null,
        val passportPhoto: Bitmap? = null,
    )

    data class AddressDetails(
        val providenceId: String? = null,
        val cityId: String? = null,
        val streetId: String? = null,
    )
}