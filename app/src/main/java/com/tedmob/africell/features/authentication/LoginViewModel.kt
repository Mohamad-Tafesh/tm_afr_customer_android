package com.tedmob.africell.features.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.SingleLiveEvent
import com.tedmob.africell.data.entity.Country
import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.authentication.domain.GetCountriesUseCase
import com.tedmob.africell.features.authentication.domain.LoginUseCase
import com.tedmob.africell.ui.BaseViewModel
import javax.inject.Inject


class LoginViewModel
@Inject constructor(
    private val getCountries: GetCountriesUseCase,
    private val loginUseCase: LoginUseCase,
    private val appExceptionFactory: AppExceptionFactory
) : BaseViewModel() {


    private val _loginData = SingleLiveEvent<Resource<Unit>>()
    val loginData: LiveData<Resource<Unit>> get() = _loginData

    val countriesData = MutableLiveData<Resource<List<Country>>>()

    fun getCountries() {
        ResourceUseCaseExecutor(
            getCountries,
            Unit,
            countriesData,
            appExceptionFactory,
            null,
            null
        ).execute()
    }

    fun login(mobileNumber: String, password: String) {
        val params = LoginUseCase.UserLoginInfo(mobileNumber, password)
        ResourceUseCaseExecutor(
            loginUseCase,
            params,
            _loginData,
            appExceptionFactory,
            null,
            null
        ).execute()
    }


    override fun onCleared() {
        super.onCleared()
        getCountries.dispose()
        loginUseCase.dispose()
    }
}