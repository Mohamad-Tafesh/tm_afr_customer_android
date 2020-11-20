package com.tedmob.africell.features.authentication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tedmob.africell.features.authentication.domain.VerifyPinUseCase
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.SingleLiveEvent
import com.tedmob.africell.data.api.dto.LoginDTO
import com.tedmob.africell.data.api.dto.UserDTO
import com.tedmob.africell.exception.AppExceptionFactory
import javax.inject.Inject


class VerifyPinViewModel
@Inject constructor(
    private val verifyPinUseCase: VerifyPinUseCase,
    private val appExceptionFactory: AppExceptionFactory
) : ViewModel() {


    private val _verifyData = SingleLiveEvent<Resource<UserDTO>>()
    val verifyData: SingleLiveEvent<Resource<UserDTO>> get() = _verifyData


    fun verifyPin(pin: String) {
        val params = VerifyPinUseCase.Params(pin)
        ResourceUseCaseExecutor(verifyPinUseCase, params, _verifyData, appExceptionFactory) .execute()
    }

    override fun onCleared() {
        super.onCleared()
        verifyPinUseCase.dispose()
    }
}