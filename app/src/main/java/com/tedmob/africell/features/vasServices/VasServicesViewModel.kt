package com.tedmob.africell.features.vasServices

import androidx.lifecycle.MutableLiveData
import com.tedmob.africell.app.AppSessionNavigator
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.api.dto.LocationDTO
import com.tedmob.africell.data.api.dto.ServicesDTO
import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.location.domain.GetLocationsUseCase
import com.tedmob.africell.features.services.domain.GetServicesUseCase
import com.tedmob.africell.features.vasServices.domain.GetVasServicesUseCase

import com.tedmob.africell.ui.BaseViewModel
import javax.inject.Inject

class VasServicesViewModel
@Inject constructor(
    private val getServicesUseCase: GetVasServicesUseCase,
        private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {

    val serviceData = MutableLiveData<Resource<List<ServicesDTO>>>()

    fun getServices(){
        ResourceUseCaseExecutor(getServicesUseCase, Unit, serviceData,appExceptionFactory, appSessionNavigator) {
            getServices()
        }.execute()

    }


    override fun onCleared() {
        getServicesUseCase.dispose()
        super.onCleared()
    }
}
