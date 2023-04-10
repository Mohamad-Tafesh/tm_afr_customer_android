package com.africell.africell.features.vasServices

import androidx.lifecycle.MutableLiveData
import com.africell.africell.app.AppSessionNavigator
import com.africell.africell.app.ResourceUseCaseExecutor
import com.africell.africell.data.Resource
import com.africell.africell.data.api.dto.ServicesDTO
import com.africell.africell.exception.AppExceptionFactory
import com.africell.africell.features.vasServices.domain.GetVasServicesUseCase

import com.africell.africell.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
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
