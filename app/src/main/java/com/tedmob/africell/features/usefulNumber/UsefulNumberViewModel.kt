package com.tedmob.africell.features.usefulNumber

import androidx.lifecycle.MutableLiveData
import com.tedmob.africell.app.AppSessionNavigator
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.api.dto.LocationDTO
import com.tedmob.africell.data.api.dto.ServicesDTO
import com.tedmob.africell.data.api.dto.UsefulNumberDTO
import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.location.domain.GetLocationsUseCase
import com.tedmob.africell.features.services.domain.GetServicesUseCase
import com.tedmob.africell.features.usefulNumber.domain.GetUsefulUseCase

import com.tedmob.africell.ui.BaseViewModel
import javax.inject.Inject

class UsefulNumberViewModel
@Inject constructor(
    private val getUsefulUseCase: GetUsefulUseCase,
        private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {

    val usefulData = MutableLiveData<Resource<List<UsefulNumberDTO>>>()

     fun getUsefulNumber(){
        ResourceUseCaseExecutor(getUsefulUseCase, Unit, usefulData,appExceptionFactory, appSessionNavigator) {
            getUsefulNumber()
        }.execute()

    }


    override fun onCleared() {
        getUsefulUseCase.dispose()
        super.onCleared()
    }
}
