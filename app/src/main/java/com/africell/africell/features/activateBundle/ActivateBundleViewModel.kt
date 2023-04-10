package com.africell.africell.features.activateBundle

import androidx.lifecycle.MutableLiveData
import com.africell.africell.app.AppSessionNavigator
import com.africell.africell.app.ResourceUseCaseExecutor
import com.africell.africell.data.Resource
import com.africell.africell.data.SingleLiveEvent
import com.africell.africell.data.api.dto.StatusDTO
import com.africell.africell.data.api.requests.ActivateBundleRequest
import com.africell.africell.data.entity.Country
import com.africell.africell.data.entity.SubAccount
import com.africell.africell.exception.AppExceptionFactory
import com.africell.africell.features.accountInfo.domain.GetSubAccountUseCase
import com.africell.africell.features.activateBundle.domain.ActivateBundleUseCase
import com.africell.africell.features.authentication.domain.GetCountriesUseCase
import com.africell.africell.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
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
