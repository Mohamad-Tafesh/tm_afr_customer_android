package com.africell.africell.features.authentication

import androidx.lifecycle.ViewModel
import com.africell.africell.app.AppSessionNavigator
import com.africell.africell.features.authentication.domain.VerifyPinUseCase
import com.africell.africell.app.ResourceUseCaseExecutor
import com.africell.africell.data.Resource
import com.africell.africell.data.SingleLiveEvent
import com.africell.africell.data.api.dto.VerifyOTPDTO
import com.africell.africell.exception.AppExceptionFactory
import javax.inject.Inject


class VerifyPinViewModel
@Inject constructor(
    private val verifyPinUseCase: VerifyPinUseCase,
        private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : ViewModel() {


    private val _verifyData = SingleLiveEvent<Resource<VerifyOTPDTO>>()
    val verifyData: SingleLiveEvent<Resource<VerifyOTPDTO>> get() = _verifyData


    fun verifyPin(pin: String) {
        val params = VerifyPinUseCase.Params(pin)
        ResourceUseCaseExecutor(verifyPinUseCase, params, _verifyData,appExceptionFactory, appSessionNavigator) .execute()
    }

    override fun onCleared() {
        super.onCleared()
        verifyPinUseCase.dispose()
    }
}