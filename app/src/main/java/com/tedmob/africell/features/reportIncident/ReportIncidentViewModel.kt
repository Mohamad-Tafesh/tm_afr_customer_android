package com.tedmob.africell.features.reportIncident

import androidx.lifecycle.MutableLiveData
import com.tedmob.africell.app.AppSessionNavigator
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.SingleLiveEvent
import com.tedmob.africell.data.api.dto.IncidentTypeDTO
import com.tedmob.africell.data.api.dto.StatusDTO
import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.reportIncident.domain.GetReportCategoryUseCase
import com.tedmob.africell.features.reportIncident.domain.ReportIncidentUseCase
import com.tedmob.africell.ui.BaseViewModel
import javax.inject.Inject


class ReportIncidentViewModel
@Inject constructor(
    private val reportIncidentUseCase: ReportIncidentUseCase,
    private val getReportIncidentUseCase: GetReportCategoryUseCase,
        private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {


    val contactUsData = SingleLiveEvent<Resource<StatusDTO>>()
    val supportCategoryData = MutableLiveData<Resource<List<IncidentTypeDTO>>>()

    fun getSupportCategory() {
        ResourceUseCaseExecutor(
            getReportIncidentUseCase,
            Unit,
            supportCategoryData,
            appExceptionFactory,appSessionNavigator
        ) {
            getSupportCategory()
        }.execute()
    }

    fun contactUs( category:IncidentTypeDTO?, message: String) {
        ResourceUseCaseExecutor(
            reportIncidentUseCase,
            ReportIncidentUseCase.Params(category?.idincidentType, category?.incidentTypeName, message),
            contactUsData,
            appExceptionFactory,appSessionNavigator

        ).execute()
    }


    override fun onCleared() {
        super.onCleared()
        getReportIncidentUseCase.dispose()
        reportIncidentUseCase.dispose()
    }
}