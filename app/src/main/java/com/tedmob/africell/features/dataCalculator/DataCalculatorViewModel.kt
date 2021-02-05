package com.tedmob.africell.features.dataCalculator

import androidx.lifecycle.MutableLiveData
import com.tedmob.africell.app.AppSessionNavigator
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.SingleLiveEvent
import com.tedmob.africell.data.api.dto.DataCalculatorDTO
import com.tedmob.africell.data.api.dto.StatusDTO
import com.tedmob.africell.data.api.dto.SupportCategoryDTO
import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.dataCalculator.domain.GetDataCalculatorUseCase
import com.tedmob.africell.features.reportIncident.domain.GetReportCategoryUseCase
import com.tedmob.africell.features.reportIncident.domain.ReportIncidentUseCase
import com.tedmob.africell.ui.BaseViewModel
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