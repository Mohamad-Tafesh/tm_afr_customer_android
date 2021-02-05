package com.tedmob.africell.features.location

import androidx.lifecycle.MutableLiveData
import com.tedmob.africell.app.AppSessionNavigator
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.api.dto.LocationDTO
import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.location.domain.GetLocationsUseCase

import com.tedmob.africell.ui.BaseViewModel
import javax.inject.Inject

class LocationViewModel
@Inject constructor(
    private val getLocationsUseCase: GetLocationsUseCase,
        private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {

    val locationData = MutableLiveData<Resource<List<LocationDTO>>>()

    fun getLocations(search:String?, lat: Double?, long: Double?) {
        getLocationsUseCase.clear()
        ResourceUseCaseExecutor(getLocationsUseCase, GetLocationsUseCase.Params(search, lat, long), locationData,appExceptionFactory, appSessionNavigator) {
            getLocations(search, lat, long)
        }.execute()

    }


    override fun onCleared() {
        getLocationsUseCase.dispose()
        super.onCleared()
    }
}
