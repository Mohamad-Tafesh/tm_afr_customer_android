package com.tedmob.africell.features.bookNumber

import androidx.lifecycle.MutableLiveData
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource

import com.tedmob.africell.data.api.dto.LocationDTO
import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.bookNumber.domain.GetBookNumberUseCase
import com.tedmob.africell.features.location.domain.GetLocationsUseCase

import com.tedmob.africell.ui.BaseViewModel
import javax.inject.Inject

class BookNumberViewModel
@Inject constructor(
    private val getBookNumberUseCase: GetBookNumberUseCase,
    private val appExceptionFactory: AppExceptionFactory
) : BaseViewModel() {

    val locationData = MutableLiveData<Resource<List<String>>>()

    fun getBookNumber(search:String?) {
        ResourceUseCaseExecutor(getBookNumberUseCase,GetBookNumberUseCase.Params(search), locationData, appExceptionFactory) {
            getBookNumber(search)
        }.execute()
    }

    override fun onCleared() {
        getBookNumberUseCase.dispose()
        super.onCleared()
    }
}
