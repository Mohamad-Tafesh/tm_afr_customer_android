package com.africell.africell.features.reportIncident

import androidx.lifecycle.MutableLiveData
import com.africell.africell.app.AppSessionNavigator
import com.africell.africell.app.ResourceUseCaseExecutor
import com.tedmob.afrimoney.data.Resource
import com.africell.africell.data.SingleLiveEvent
import com.africell.africell.data.api.dto.IncidentTypeDTO
import com.africell.africell.data.api.dto.StatusDTO
import com.africell.africell.exception.AppExceptionFactory
import com.africell.africell.features.reportIncident.domain.GetReportCategoryUseCase
import com.africell.africell.features.reportIncident.domain.ReportIncidentUseCase
import com.africell.africell.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
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