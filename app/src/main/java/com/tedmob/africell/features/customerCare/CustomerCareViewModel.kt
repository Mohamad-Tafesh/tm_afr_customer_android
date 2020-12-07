package com.tedmob.africell.features.customerCare

import androidx.lifecycle.MutableLiveData
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.SingleLiveEvent
import com.tedmob.africell.data.api.dto.StatusDTO
import com.tedmob.africell.data.api.dto.SupportCategoryDTO
import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.customerCare.domain.GetCustomerCareCategoryUseCase
import com.tedmob.africell.features.customerCare.domain.SubmitCustomerCareUseCase
import com.tedmob.africell.ui.BaseViewModel
import javax.inject.Inject


class CustomerCareViewModel
@Inject constructor(
    private val submitCustomerCareUseCase: SubmitCustomerCareUseCase,
    private val getCustomerCareCategoryUseCase: GetCustomerCareCategoryUseCase,
    private val appExceptionFactory: AppExceptionFactory
) : BaseViewModel() {


    val contactUsData = SingleLiveEvent<Resource<StatusDTO>>()
    val supportCategoryData = MutableLiveData<Resource<List<SupportCategoryDTO>>>()

    fun getSupportCategory() {
        ResourceUseCaseExecutor(
            getCustomerCareCategoryUseCase,
            Unit,
            supportCategoryData,
            appExceptionFactory
        ) {
            getSupportCategory()
        }.execute()
    }

    fun contactUs( category: SupportCategoryDTO?, message: String) {
        ResourceUseCaseExecutor(
            submitCustomerCareUseCase,
            SubmitCustomerCareUseCase.Params( category?.id,category?.name, message),
            contactUsData,
            appExceptionFactory
        ).execute()
    }


    override fun onCleared() {
        super.onCleared()
        getCustomerCareCategoryUseCase.dispose()
        submitCustomerCareUseCase.dispose()
    }
}