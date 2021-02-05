package com.tedmob.africell.features.services

import androidx.lifecycle.MutableLiveData
import com.tedmob.africell.app.AppSessionNavigator
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.SingleLiveEvent
import com.tedmob.africell.data.api.dto.LocationDTO
import com.tedmob.africell.data.api.dto.ServicesDTO
import com.tedmob.africell.data.api.dto.StatusDTO
import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.location.domain.GetLocationsUseCase
import com.tedmob.africell.features.services.domain.GetServicesUseCase
import com.tedmob.africell.features.services.domain.ServiceSubscribeUseCase
import com.tedmob.africell.features.services.domain.ServiceUnSubscribeUseCase
import com.tedmob.africell.features.vasServices.domain.GetVasServicesUseCase

import com.tedmob.africell.ui.BaseViewModel
import javax.inject.Inject

class ServicesViewModel
@Inject constructor(
    private val getVasServicesUseCase: GetVasServicesUseCase,
    private val subscribeUseCase: ServiceSubscribeUseCase,
    private val unSubscribeUseCase: ServiceUnSubscribeUseCase,
        private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {

    val serviceData = MutableLiveData<Resource<List<ServicesDTO>>>()
    val subscribeData= SingleLiveEvent<Resource<StatusDTO>>()
    val unSubscribeData= SingleLiveEvent<Resource<StatusDTO>>()

    fun getServices(){
        ResourceUseCaseExecutor(getVasServicesUseCase, Unit, serviceData,appExceptionFactory, appSessionNavigator) {
            getServices()
        }.execute()

    }

    fun subscribe( sname:String){
        ResourceUseCaseExecutor(subscribeUseCase, sname, subscribeData,appExceptionFactory, appSessionNavigator) .execute()

    }
    fun unsubscribe(sname:String){
        ResourceUseCaseExecutor(unSubscribeUseCase, sname, unSubscribeData,appExceptionFactory, appSessionNavigator).execute()

    }

    override fun onCleared() {
        getVasServicesUseCase.dispose()
        super.onCleared()
    }
}
