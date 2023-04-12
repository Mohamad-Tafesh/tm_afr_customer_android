package com.tedmob.afrimoney.features.account

import androidx.lifecycle.LiveData
import com.tedmob.afrimoney.data.Resource
import com.tedmob.afrimoney.data.SingleLiveEvent
import com.tedmob.afrimoney.data.entity.MyAddressFields
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.features.account.domain.GetAddressFieldsUseCase
import com.tedmob.afrimoney.ui.BaseViewModel
import com.tedmob.modules.mapcontainer.view.MapLatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyAddressViewModel
@Inject constructor(
    private val getAddressFieldsUseCase: GetAddressFieldsUseCase,
    private val appExceptionFactory: AppExceptionFactory,
) : BaseViewModel() {

    var coordinates: MapLatLng? = null


    val data: LiveData<Resource<MyAddressFields>> get() = _data
    private val _data = SingleLiveEvent<Resource<MyAddressFields>>()


    fun getData() {
        if (_data.value is Resource.Success) {
            _data.value = Resource.Success((_data.value as Resource.Success).data)
        } else {
            executeResource(
                getAddressFieldsUseCase,
                Unit,
                _data,
                appExceptionFactory,
                action = { getData() },
            )
        }
    }
}