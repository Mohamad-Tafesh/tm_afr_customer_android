package com.africell.africell.features.usefulNumber

import androidx.lifecycle.MutableLiveData
import com.africell.africell.app.AppSessionNavigator
import com.africell.africell.app.ResourceUseCaseExecutor
import com.tedmob.afrimoney.data.Resource
import com.africell.africell.data.api.dto.UsefulNumberDTO
import com.africell.africell.exception.AppExceptionFactory
import com.africell.africell.features.usefulNumber.domain.GetUsefulUseCase

import com.africell.africell.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
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
