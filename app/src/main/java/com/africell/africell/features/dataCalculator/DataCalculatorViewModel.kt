package com.africell.africell.features.dataCalculator

import androidx.lifecycle.MutableLiveData
import com.africell.africell.app.AppSessionNavigator
import com.africell.africell.app.ResourceUseCaseExecutor
import com.africell.africell.data.Resource
import com.africell.africell.data.api.dto.DataCalculatorDTO
import com.africell.africell.exception.AppExceptionFactory
import com.africell.africell.features.dataCalculator.domain.GetDataCalculatorUseCase
import com.africell.africell.ui.BaseViewModel
import javax.inject.Inject


class DataCalculatorViewModel
@Inject constructor(
    private val getDataCalculatorUseCase: GetDataCalculatorUseCase,
        private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {


    val dataCalculatorData = MutableLiveData<Resource<DataCalculatorDTO>>()

    fun getDataCalculator() {
        ResourceUseCaseExecutor(
            getDataCalculatorUseCase,
            Unit,
            dataCalculatorData,
            appExceptionFactory,appSessionNavigator
        ) {
            getDataCalculator()
        }.execute()
    }


    override fun onCleared() {
        super.onCleared()
        getDataCalculatorUseCase.dispose()
    }
}