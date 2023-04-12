package com.tedmob.afrimoney.features.authentication

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import com.tedmob.afrimoney.data.*
import com.tedmob.afrimoney.data.entity.UserState
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.features.authentication.domain.RegisterUseCase
import com.tedmob.afrimoney.ui.BaseViewModel
import com.tedmob.afrimoney.util.security.StringEncryptor
import com.tedmob.afrimoney.util.security.encrypted
import com.tedmob.modules.mapcontainer.view.MapLatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class RegisterViewModel
@Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    @Named("local-string") private val encryptor: StringEncryptor,
) : BaseViewModel() {

    private val data: RegisterStepsData = RegisterStepsData()

    var code: String by encrypted(encryptor, "")

    val proceedToStep2: LiveData<Resource<Unit>> get() = _proceedToStep2
    private val _proceedToStep2 = SingleLiveEvent<Resource<Unit>>()

    val proceedToPin: LiveData<Resource<UserState>> get() = _proceedToPin
    private val _proceedToPin = SingleLiveEvent<Resource<UserState>>()


    fun hasAddress(): Boolean = data.addressCoordinates != null

    fun setAddress(
        coordinates: MapLatLng,
        providenceId: String? = null,
        cityId: String? = null,
        streetId: String? = null,
    ) {
        data.addressCoordinates = coordinates
        data.addressDetails = RegisterStepsData.AddressDetails(
            providenceId,
            cityId,
            streetId,
        )
    }

    fun registerStep1(
        // image: Bitmap?,
        firstName: String,
        lastName: String,
        gender: String?,
        dob: Calendar?,
        //invitationCode: String?,
    ) {

        addJob("registerStep1") {
            data.step1 = RegisterStepsData.Step1(
                //image,
                firstName,
                lastName,
                gender,
                dob,
                //invitationCode,
            )
            _proceedToStep2.emitSuccess(Unit)
        }
    }



    fun test():String{
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val dob = sdf.format(data.step1.dob!!.time)
        val date=dob.split("/")
        val d=date.get(0)+date.get(1)+date.get(2)
        return d
    }

    fun registerStep2(
        mobilenb:String,
        idNumber: String,
        idType: String,
       // frontImage: Bitmap,
      //  backImage: Bitmap,
      //  passportPhoto: Bitmap
    ) {

        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val dob = sdf.format(data.step1.dob!!.time)
        val date=dob.split("/")
        val d=date.get(0)+date.get(1)+date.get(2)
        execute(
            registerUseCase,
            RegisterUseCase.UserLoginInfo(
                mobilenb,
                data.step1.firstName,
                data.step1.lastName,
                idNumber,
                idType,
                d,
                data.step1.gender.toString(),
                data.addressDetails?.streetId.toString(),
                data.addressDetails?.cityId.toString(),
                data.addressDetails?.providenceId.toString()


            ),


            onLoading = {
                _proceedToPin.emitLoading()
            },
            onError = {
                appExceptionFactory.make(it)
                    .let { exception ->
                        _proceedToPin.emitError(exception.userMessage, null)
                    }
            },
            onSuccess = {
                _proceedToPin.emitSuccess(it)
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        code = ""
        data.run {
            step1 = RegisterStepsData.Step1()
            step2 = RegisterStepsData.Step2()
        }
    }
}