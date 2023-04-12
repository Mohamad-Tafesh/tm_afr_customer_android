package com.africell.africell.features.customerCare

import androidx.lifecycle.MutableLiveData
import com.africell.africell.app.AppSessionNavigator
import com.africell.africell.app.ResourceUseCaseExecutor
import com.tedmob.afrimoney.data.Resource
import com.africell.africell.data.SingleLiveEvent
import com.africell.africell.data.api.dto.StatusDTO
import com.africell.africell.data.api.dto.SupportCategoryDTO
import com.africell.africell.exception.AppExceptionFactory
import com.africell.africell.features.customerCare.domain.GetCustomerCareCategoryUseCase
import com.africell.africell.features.customerCare.domain.SubmitCustomerCareUseCase
import com.africell.africell.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class CustomerCareViewModel
@Inject constructor(
    private val submitCustomerCareUseCase: SubmitCustomerCareUseCase,
    private val getCustomerCareCategoryUseCase: GetCustomerCareCategoryUseCase,
        private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {


    val contactUsData = SingleLiveEvent<Resource<StatusDTO>>()
    val supportCategoryData = MutableLiveData<Resource<List<SupportCategoryDTO>>>()

    fun getSupportCategory() {
        ResourceUseCaseExecutor(
            getCustomerCareCategoryUseCase,
            Unit,
            supportCategoryData,
            appExceptionFactory,appSessionNavigator
        ) {
            getSupportCategory()
        }.execute()
    }

    fun contactUs( category: SupportCategoryDTO?, message: String) {
        ResourceUseCaseExecutor(
            submitCustomerCareUseCase,
            SubmitCustomerCareUseCase.Params( category?.id,category?.name, message),
            contactUsData,
            appExceptionFactory,appSessionNavigator
        ).execute()
    }


    override fun onCleared() {
        super.onCleared()
        getCustomerCareCategoryUseCase.dispose()
        submitCustomerCareUseCase.dispose()
    }
}