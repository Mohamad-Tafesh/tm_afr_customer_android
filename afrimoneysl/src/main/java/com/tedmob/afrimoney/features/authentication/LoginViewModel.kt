package com.tedmob.afrimoney.features.authentication

import androidx.lifecycle.LiveData
import com.tedmob.afrimoney.data.*
import com.tedmob.afrimoney.data.api.dto.GenerateOtpDTO
import com.tedmob.afrimoney.data.entity.UserState
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.features.authentication.domain.LoginUseCase
import com.tedmob.afrimoney.features.authentication.domain.VerifyLoginUseCase
import com.tedmob.afrimoney.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val verifyLogin: VerifyLoginUseCase,
    private val appExceptionFactory: AppExceptionFactory,
) : BaseViewModel() {


    private val _loginData = SingleLiveEvent<Resource<GenerateOtpDTO>>()
    val loginData: LiveData<Resource<GenerateOtpDTO>> get() = _loginData

    private val _resenddata = SingleLiveEvent<Resource<GenerateOtpDTO>>()
    val resenddata: LiveData<Resource<GenerateOtpDTO>> get() = _resenddata

    val verified: LiveData<Resource<UserState>> get() = _verified
    private val _verified = SingleLiveEvent<Resource<UserState>>()


    fun login(mobileNumber: String) {
        execute(
            loginUseCase,
            LoginUseCase.UserLoginInfo(mobileNumber),
            onLoading = {
                _loginData.emitLoading()
            },
            onError = {
                appExceptionFactory.make(it)
                    .let { exception ->
                        _loginData.emitError(exception.userMessage, null)
                    }
            },
            onSuccess = {
                _loginData.emitSuccess(it)
            }
        )
    }

    fun resend(mobileNumber: String) {
        execute(
            loginUseCase,
            LoginUseCase.UserLoginInfo(mobileNumber),
            onLoading = {
                _resenddata.emitLoading()
            },
            onError = {
                appExceptionFactory.make(it)
                    .let { exception ->
                        _resenddata.emitError(exception.userMessage, null)
                    }
            },
            onSuccess = {
                _resenddata.emitSuccess(it)
            }
        )
    }


    fun verify(pin: String, mobilenb: String) {
        executeResource(
            verifyLogin,
            VerifyLoginUseCase.UserLoginInfo(pin, mobilenb),
            _verified,
            appExceptionFactory,
            null,
            null
        )
    }


    override fun onCleared() {
        super.onCleared()
    }
}