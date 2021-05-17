package com.tedmob.africell.features.bundles

import androidx.lifecycle.MutableLiveData
import com.tedmob.africell.app.AppSessionNavigator
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource

import com.tedmob.africell.data.api.dto.BundleInfo
import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.bundles.domain.GetBundleDetailsUseCase

import com.tedmob.africell.ui.BaseViewModel
import javax.inject.Inject

class BundleDetailsViewModel
@Inject constructor(
    private val getBundleDetailsUseCase: GetBundleDetailsUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {


    val bundlesData = MutableLiveData<Resource<BundleInfo>>()


    fun getBundlesDetails(bundleId: Long?) {
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
