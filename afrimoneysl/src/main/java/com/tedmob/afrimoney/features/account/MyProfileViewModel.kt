package com.tedmob.afrimoney.features.account

import androidx.lifecycle.LiveData
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.data.Resource
import com.tedmob.afrimoney.data.SingleLiveEvent
import com.tedmob.afrimoney.data.entity.Profile
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.features.account.domain.GetMyProfileUseCase
import com.tedmob.afrimoney.ui.BaseViewModel
import com.tedmob.modules.mapcontainer.view.MapLatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel
@Inject constructor(
    private val getMyProfileUseCase: GetMyProfileUseCase,
    //...
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator,
) : BaseViewModel() {

    class AddressDetails(
        val providenceId: String? = null,
        val cityId: String? = null,
        val streetId: String? = null,
    )


    var addressCoordinates: MapLatLng? = null
    var addressDetails: AddressDetails? = null

    fun hasAddress(): Boolean = addressCoordinates != null

    fun setAddress(
        coordinates: MapLatLng,
        providenceId: String? = null,
        cityId: String? = null,
        streetId: String? = null,
    ) {
        this.addressCoordinates = coordinates
        this.addressDetails = AddressDetails(
            providenceId,
            cityId,
            streetId,
        )
    }


    val profile: LiveData<Resource<Profile>> get() = _profile
    private val _profile = SingleLiveEvent<Resource<Profile>>()

    //...


    fun getProfile() {
        executeResource(
            getMyProfileUseCase,
            Unit,
            _profile,
            appExceptionFactory,
            appSessionNavigator,
            action = { getProfile() },
        )
    }

    //...


    override fun onCleared() {
        super.onCleared()
        //...
    }
}