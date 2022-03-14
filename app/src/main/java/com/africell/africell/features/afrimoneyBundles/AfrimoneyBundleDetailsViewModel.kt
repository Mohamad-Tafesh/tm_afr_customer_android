package com.africell.africell.features.afrimoneyBundles

import androidx.lifecycle.MutableLiveData
import com.africell.africell.app.AppSessionNavigator
import com.africell.africell.app.ResourceUseCaseExecutor
import com.africell.africell.data.Resource

import com.africell.africell.data.api.dto.BundleInfo
import com.africell.africell.exception.AppExceptionFactory
import com.africell.africell.features.afrimoneyBundles.domain.GetAfrimoneyBundleDetailsUseCase
import com.africell.africell.features.bundles.domain.GetBundleDetailsUseCase

import com.africell.africell.ui.BaseViewModel
import javax.inject.Inject

class AfrimoneyBundleDetailsViewModel
@Inject constructor(
    private val getBundleDetailsUseCase: GetAfrimoneyBundleDetailsUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {


    val bundlesData = MutableLiveData<Resource<BundleInfo>>()


    fun getBundlesDetails(bundleId: String) {
        ResourceUseCaseExecutor(
            getBundleDetailsUseCase,
            GetAfrimoneyBundleDetailsUseCase.Params(bundleId),
            bundlesData,
            appExceptionFactory,
            appSessionNavigator
        ) {
            getBundlesDetails(bundleId)
        }.execute()
    }

    override fun onCleared() {
        getBundleDetailsUseCase.dispose()
        super.onCleared()
    }
}
