package com.tedmob.africell.features.addNewAccount

import androidx.lifecycle.MutableLiveData
import com.tedmob.africell.app.AppSessionNavigator
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.SingleLiveEvent
import com.tedmob.africell.data.api.dto.GenerateOTPDTO
import com.tedmob.africell.data.api.dto.SubAccountDTO
import com.tedmob.africell.data.entity.Country
import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.addNewAccount.domain.AddSubAccountUseCase
import com.tedmob.africell.features.authentication.domain.GenerateOTPUseCase
import com.tedmob.africell.features.authentication.domain.GetCountriesUseCase
import com.tedmob.africell.ui.BaseViewModel
import javax.inject.Inject


class AddAccountViewModel
@Inject constructor(
    private val getCountries: GetCountriesUseCase,
    private val addSubAccountUseCase: AddSubAccountUseCase,
    private val generateOTPUseCase: GenerateOTPUseCase,
        private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {


    val countriesData = MutableLiveData<Resource<List<Country>>>()
    val generateOTPData = SingleLiveEvent<Resource<GenerateOTPDTO>>()
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

    fun generateOTP(mobileNumber: String, typeOTP:Int) {
        val params = GenerateOTPUseCase.Params(mobileNumber,typeOTP)
        ResourceUseCaseExecutor(
            generateOTPUseCase,
            params,
            generateOTPData,
            appExceptionFactory,
            appSessionNavigator,
            null
        ).execute()

    }

    val verifyData = SingleLiveEvent<Resource<SubAccountDTO>>()


    fun verifyPin(pin: String) {
        val params = AddSubAccountUseCase.Params(pin)
        ResourceUseCaseExecutor(addSubAccountUseCase, params, verifyData,appExceptionFactory, appSessionNavigator).execute()
    }

    override fun onCleared() {
        super.onCleared()
        getCountries.dispose()
        generateOTPUseCase.dispose()
    }
}