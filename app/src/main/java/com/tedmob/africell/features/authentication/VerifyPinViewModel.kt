package com.tedmob.africell.features.authentication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tedmob.africell.app.AppSessionNavigator
import com.tedmob.africell.features.authentication.domain.VerifyPinUseCase
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.SingleLiveEvent
import com.tedmob.africell.data.api.dto.LoginDTO
import com.tedmob.africell.data.api.dto.UserDTO
import com.tedmob.africell.data.api.dto.VerifyOTPDTO
import com.tedmob.africell.exception.AppExceptionFactory
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