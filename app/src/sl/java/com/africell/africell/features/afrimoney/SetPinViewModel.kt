package com.africell.africell.features.afrimoney

import androidx.lifecycle.LiveData
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.app.handleInvalidSession
import com.tedmob.afrimoney.data.*
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.features.account.domain.LogoutUseCase
import com.tedmob.afrimoney.features.authentication.domain.SetPinUseCase
import com.tedmob.afrimoney.features.authentication.domain.VerifyLoginUseCase
import com.tedmob.afrimoney.features.authentication.domain.VerifyPinUseCase
import com.tedmob.afrimoney.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SetPinViewModel
@Inject constructor(
    private val verifyPin: VerifyPinUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {

    val pinEntered: LiveData<Resource<Unit>> get() = _pinEntered
    private val _pinEntered = SingleLiveEvent<Resource<Unit>>()

    val loggedOut: LiveData<Resource<Unit>> get() = _loggedOut
    private val _loggedOut = SingleLiveEvent<Resource<Unit>>()

    fun enterPin(mobilenb:String,pin: String) {
        execute(
            verifyPin,
            VerifyPinUseCase.UserLoginInfo(pin, mobilenb),
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

    fun logout() {
        executeResource(
            logoutUseCase,
            Unit,
            _loggedOut,
            appExceptionFactory,
            appSessionNavigator,
        )
    }


    override fun onCleared() {
        super.onCleared()
        //...
    }
}