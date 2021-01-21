package com.tedmob.africell.features.bundles

import androidx.lifecycle.MutableLiveData
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource

import com.tedmob.africell.data.api.dto.BundlesDTO
import com.tedmob.africell.data.api.dto.LocationDTO
import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.bookNumber.domain.GetBookNumberUseCase
import com.tedmob.africell.features.bundles.domain.GetBundlesUseCase
import com.tedmob.africell.features.location.domain.GetLocationsUseCase

import com.tedmob.africell.ui.BaseViewModel
import javax.inject.Inject

class BundlesViewModel
@Inject constructor(
    private val getBundlesUseCase: GetBundlesUseCase,
    private val appExceptionFactory: AppExceptionFactory
) : BaseViewModel() {

    val bundleData = MutableLiveData<Resource<List<BundlesDTO>>>()

    fun getBundles() {
        ResourceUseCaseExecutor(getBundlesUseCase,Unit, bundleData, appExceptionFactory) {
            getBundles()
        }.execute()
    }

    override fun onCleared() {
        getBundlesUseCase.dispose()
        super.onCleared()
    }
}
