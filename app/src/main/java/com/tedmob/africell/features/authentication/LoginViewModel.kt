package com.tedmob.africell.features.authentication

import androidx.lifecycle.MutableLiveData
import com.tedmob.africell.app.AppSessionNavigator
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.SingleLiveEvent
import com.tedmob.africell.data.api.dto.GenerateOTPDTO
import com.tedmob.africell.data.entity.Country
import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.authentication.domain.GenerateOTPUseCase
import com.tedmob.africell.features.authentication.domain.GetCountriesUseCase
import com.tedmob.africell.features.authentication.domain.LoginUseCase
import com.tedmob.africell.ui.BaseViewModel
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