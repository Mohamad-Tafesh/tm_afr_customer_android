package com.tedmob.africell.features.activateBundle

import androidx.lifecycle.MutableLiveData
import com.tedmob.africell.app.AppSessionNavigator
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.SingleLiveEvent
import com.tedmob.africell.data.api.dto.StatusDTO
import com.tedmob.africell.data.api.requests.ActivateBundleRequest
import com.tedmob.africell.data.entity.Country
import com.tedmob.africell.data.entity.SubAccount
import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.accountInfo.domain.GetSubAccountUseCase
import com.tedmob.africell.features.activateBundle.domain.ActivateBundleUseCase
import com.tedmob.africell.features.authentication.domain.GetCountriesUseCase
import com.tedmob.africell.ui.BaseViewModel
import javax.inject.Inject

class ActivateBundleViewModel
@Inject constructor(
    private val activateBundleUseCase: ActivateBundleUseCase,
    private val getCountries: GetCountriesUseCase,
    private val getSubAccountUseCase: GetSubAccountUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {


    val activateBundleData = SingleLiveEvent<Resource<StatusDTO>>()
    val subAccountData = MutableLiveData<Resource<List<SubAccount>>>()
    val countriesData = MutableLiveData<Resource<List<Country>>>()
    fun activateBundle(activateBundle: ActivateBundleRequest) {
        ResourceUseCaseExecutor(
            activateBundleUseCase,
            activateBundle,
            activateBundleData,
            appExceptionFactory,appSessionNavigator).execute()
    }

    fun getSubAccounts() {
        ResourceUseCaseExecutor(getSubAccountUseCase, Unit, subAccountData,appExceptionFactory, appSessionNavigator) {
            getSubAccounts()
        }.execute()
    }
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

    override fun onCleared() {
        activateBundleUseCase.dispose()
        getSubAccountUseCase.dispose()
        super.onCleared()
    }
}
