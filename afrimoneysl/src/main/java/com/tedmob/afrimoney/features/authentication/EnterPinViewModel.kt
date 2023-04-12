package com.tedmob.afrimoney.features.authentication

import androidx.lifecycle.LiveData
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.app.handleInvalidSession
import com.tedmob.afrimoney.data.*
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.features.authentication.domain.VerifyPinUseCase
import com.tedmob.afrimoney.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EnterPinViewModel
@Inject constructor(
    private val verifyPin: VerifyPinUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {

    val pinEntered: LiveData<Resource<Unit>> get() = _pinEntered
    private val _pinEntered = SingleLiveEvent<Resource<Unit>>()


    fun enterPin(pin: String,msisdn:String) {
        execute(
            verifyPin,
            VerifyPinUseCase.UserLoginInfo(pin,msisdn),
            onLoading = {
                _pinEntered.emitLoading()
            },
            onSuccess = {
                _pinEntered.emitSuccess(Unit)
            },
            onError = {
                val exception = appExceptionFactory.make(it)
                if (!exception.handleInvalidSession(appSessionNavigator)) {
                    _pinEntered.emitError(exception.userMessage, null)
                }
            }
        )
    }


    override fun onCleared() {
        super.onCleared()
        //...
    }
}