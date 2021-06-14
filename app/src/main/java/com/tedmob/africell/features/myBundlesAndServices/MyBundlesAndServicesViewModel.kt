package com.tedmob.africell.features.myBundlesAndServices

import androidx.lifecycle.MutableLiveData
import com.tedmob.africell.app.AppSessionNavigator
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource

import com.tedmob.africell.data.api.dto.MyBundlesAndServices
import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.myBundlesAndServices.domain.GetMyBundlesServicesUseCase

import com.tedmob.africell.ui.BaseViewModel
import javax.inject.Inject

class MyBundlesAndServicesViewModel
@Inject constructor(
    private val getMyBundlesServicesUseCase: GetMyBundlesServicesUseCase,
        private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {

    val myBundlesAndServicesData = MutableLiveData<Resource<List<MyBundlesAndServices>>>()

    fun getMyBundlesService() {
        ResourceUseCaseExecutor(getMyBundlesServicesUseCase,Unit, myBundlesAndServicesData,appExceptionFactory, appSessionNavigator) {
            getMyBundlesService()
        }.execute()
    }

    override fun onCleared() {

        getMyBundlesServicesUseCase.dispose()
        super.onCleared()
    }
}
