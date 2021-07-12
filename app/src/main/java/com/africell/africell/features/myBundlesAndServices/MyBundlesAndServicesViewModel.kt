package com.africell.africell.features.myBundlesAndServices

import androidx.lifecycle.MutableLiveData
import com.africell.africell.app.AppSessionNavigator
import com.africell.africell.app.ResourceUseCaseExecutor
import com.africell.africell.data.Resource

import com.africell.africell.data.api.dto.MyBundlesAndServices
import com.africell.africell.exception.AppExceptionFactory
import com.africell.africell.features.myBundlesAndServices.domain.GetMyBundlesServicesUseCase

import com.africell.africell.ui.BaseViewModel
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
