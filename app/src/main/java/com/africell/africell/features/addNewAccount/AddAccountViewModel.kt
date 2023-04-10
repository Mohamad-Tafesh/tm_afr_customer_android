package com.africell.africell.features.addNewAccount

import androidx.lifecycle.MutableLiveData
import com.africell.africell.app.AppSessionNavigator
import com.africell.africell.app.ResourceUseCaseExecutor
import com.africell.africell.data.Resource
import com.africell.africell.data.SingleLiveEvent
import com.africell.africell.data.api.dto.GenerateOTPDTO
import com.africell.africell.data.api.dto.SubAccountDTO
import com.africell.africell.data.entity.Country
import com.africell.africell.exception.AppExceptionFactory
import com.africell.africell.features.addNewAccount.domain.AddSubAccountUseCase
import com.africell.africell.features.authentication.domain.GenerateOTPUseCase
import com.africell.africell.features.authentication.domain.GetCountriesUseCase
import com.africell.africell.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
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


    fun verifyPin(msisdn:String,pin: String) {
        val params = AddSubAccountUseCase.Params(msisdn,pin)
        ResourceUseCaseExecutor(addSubAccountUseCase, params, verifyData,appExceptionFactory, appSessionNavigator).execute()
    }

    override fun onCleared() {
        super.onCleared()
        getCountries.dispose()
        generateOTPUseCase.dispose()
    }
}