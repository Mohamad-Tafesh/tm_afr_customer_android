package com.africell.africell.features.authentication

import androidx.lifecycle.MutableLiveData
import com.africell.africell.app.AppSessionNavigator
import com.africell.africell.app.ResourceUseCaseExecutor
import com.africell.africell.data.Resource
import com.africell.africell.data.SingleLiveEvent
import com.africell.africell.data.api.dto.GenerateOTPDTO
import com.africell.africell.data.entity.Country
import com.africell.africell.exception.AppExceptionFactory
import com.africell.africell.features.authentication.domain.GenerateOTPUseCase
import com.africell.africell.features.authentication.domain.GetCountriesUseCase
import com.africell.africell.features.authentication.domain.LoginUseCase
import com.africell.africell.ui.BaseViewModel
import javax.inject.Inject


class LoginViewModel
@Inject constructor(
    private val getCountries: GetCountriesUseCase,
    private val loginUseCase: LoginUseCase,
    private val generateOTPUseCase: GenerateOTPUseCase,
        private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {


    val loginData=SingleLiveEvent<Resource<Unit>>()
    val countriesData = MutableLiveData<Resource<List<Country>>>()
val generateOTPData=SingleLiveEvent<Resource<GenerateOTPDTO>>()
    fun getCountries() {
        ResourceUseCaseExecutor(
            getCountries,
            Unit,
            countriesData,
            appExceptionFactory,
            appSessionNavigator,
            null
        ).execute()
    }

    fun login(mobileNumber: String, password: String) {
        val params = LoginUseCase.UserLoginInfo(mobileNumber, password)
        ResourceUseCaseExecutor(
            loginUseCase,
            params,
            loginData,
            appExceptionFactory,
            appSessionNavigator,
            null
        ).execute()
    }

    fun generateOTP(mobileNumber:String,otp:Int) {
        val params = GenerateOTPUseCase.Params(mobileNumber,otp)
        ResourceUseCaseExecutor(
            generateOTPUseCase,
            params,
            generateOTPData,
            appExceptionFactory,
            appSessionNavigator,
            null
        ).execute()

    }

    override fun onCleared() {
        super.onCleared()
        getCountries.dispose()
        loginUseCase.dispose()
        generateOTPUseCase.dispose()
    }
}