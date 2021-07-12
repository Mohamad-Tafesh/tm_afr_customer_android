package com.africell.africell.features.bundles

import androidx.lifecycle.MutableLiveData
import com.africell.africell.app.AppSessionNavigator
import com.africell.africell.app.ResourceUseCaseExecutor
import com.africell.africell.data.Resource

import com.africell.africell.data.api.dto.BundleInfo
import com.africell.africell.exception.AppExceptionFactory
import com.africell.africell.features.bundles.domain.GetBundleDetailsUseCase

import com.africell.africell.ui.BaseViewModel
import javax.inject.Inject

class BundleDetailsViewModel
@Inject constructor(
    private val getBundleDetailsUseCase: GetBundleDetailsUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {


    val bundlesData = MutableLiveData<Resource<BundleInfo>>()


    fun getBundlesDetails(bundleId: String) {
        ResourceUseCaseExecutor(
            getBundleDetailsUseCase,
            GetBundleDetailsUseCase.Params(bundleId),
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
