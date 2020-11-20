package com.tedmob.africell.features.terms


import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.SingleLiveEvent
import com.tedmob.africell.data.api.dto.TermsDTO
import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.terms.domain.GetTermsUseCase
import com.tedmob.africell.ui.BaseViewModel
import javax.inject.Inject

class TermsViewModel
@Inject constructor(
    private val getTermsUseCase: GetTermsUseCase,
    private val appExceptionFactory: AppExceptionFactory
) : BaseViewModel() {

    val data = SingleLiveEvent<Resource<TermsDTO>>()

    fun getData() {
        ResourceUseCaseExecutor(getTermsUseCase, Unit, data, appExceptionFactory) { getData() }.execute()
    }


    override fun onCleared() {
        super.onCleared()
        getTermsUseCase.dispose()
    }
}