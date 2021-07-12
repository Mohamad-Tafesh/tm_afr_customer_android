package com.africell.africell.features.terms


import com.africell.africell.app.AppSessionNavigator
import com.africell.africell.app.ResourceUseCaseExecutor
import com.africell.africell.data.Resource
import com.africell.africell.data.SingleLiveEvent
import com.africell.africell.data.api.dto.TermsDTO
import com.africell.africell.exception.AppExceptionFactory
import com.africell.africell.features.terms.domain.GetTermsUseCase
import com.africell.africell.ui.BaseViewModel
import javax.inject.Inject

class TermsViewModel
@Inject constructor(
    private val getTermsUseCase: GetTermsUseCase,
        private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {

    val data = SingleLiveEvent<Resource<TermsDTO>>()

    fun getData() {
        ResourceUseCaseExecutor(getTermsUseCase, Unit, data,appExceptionFactory, appSessionNavigator) { getData() }.execute()
    }


    override fun onCleared() {
        super.onCleared()
        getTermsUseCase.dispose()
    }
}