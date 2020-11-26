package com.tedmob.africell.features.reportIncident

import androidx.lifecycle.MutableLiveData
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.SingleLiveEvent
import com.tedmob.africell.data.api.dto.StatusDTO
import com.tedmob.africell.data.api.dto.SupportCategoryDTO
import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.reportIncident.domain.GetReportCategoryUseCase
import com.tedmob.africell.features.reportIncident.domain.ReportIncidentUseCase
import com.tedmob.africell.ui.BaseViewModel
import javax.inject.Inject


class ReportIncidentViewModel
@Inject constructor(
    private val reportIncidentUseCase: ReportIncidentUseCase,
    private val getReportIncidentUseCase: GetReportCategoryUseCase,
    private val appExceptionFactory: AppExceptionFactory
) : BaseViewModel() {


    val contactUsData = SingleLiveEvent<Resource<StatusDTO>>()
    val supportCategoryData = MutableLiveData<Resource<List<SupportCategoryDTO>>>()

    fun getSupportCategory() {
        ResourceUseCaseExecutor(
            getReportIncidentUseCase,
            Unit,
            supportCategoryData,
            appExceptionFactory
        ) {
            getSupportCategory()
        }.execute()
    }

    fun contactUs(email: String?, categoryId: Long?, message: String) {
        ResourceUseCaseExecutor(
            reportIncidentUseCase,
            ReportIncidentUseCase.ContactInfo(email, categoryId, message),
            contactUsData,
            appExceptionFactory
        ).execute()
    }


    override fun onCleared() {
        super.onCleared()
        getReportIncidentUseCase.dispose()
        reportIncidentUseCase.dispose()
    }
}