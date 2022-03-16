package com.africell.africell.features.afrimoneyActivateBundle

import androidx.lifecycle.MutableLiveData
import com.africell.africell.app.AppSessionNavigator
import com.africell.africell.app.ResourceUseCaseExecutor
import com.africell.africell.data.Resource
import com.africell.africell.data.SingleLiveEvent
import com.africell.africell.data.api.dto.StatusDTO
import com.africell.africell.data.api.dto.WalletDTO
import com.africell.africell.data.api.requests.ActivateBundleRequest
import com.africell.africell.data.api.requests.afrimoney.AfrimoneyActivateBundleRequest
import com.africell.africell.data.api.requests.afrimoney.P2PRequest
import com.africell.africell.data.entity.Country
import com.africell.africell.data.entity.SubAccount
import com.africell.africell.exception.AppExceptionFactory
import com.africell.africell.features.accountInfo.domain.GetSubAccountUseCase
import com.africell.africell.features.activateBundle.domain.ActivateBundleUseCase
import com.africell.africell.features.afrimoney.domain.GetWalletUseCase
import com.africell.africell.features.afrimoneyActivateBundle.domain.AfrimoneyActivateBundleUseCase
import com.africell.africell.features.afrimoneyP2P.domain.P2PRequestUseCase
import com.africell.africell.features.authentication.domain.GetCountriesUseCase
import com.africell.africell.ui.BaseViewModel
import javax.inject.Inject

class AfrimoneyActivateBundleViewModel
@Inject constructor(
    private val afrimoneyActivateBundleUseCase: AfrimoneyActivateBundleUseCase,
    private val getWalletUseCase: GetWalletUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {


    val requestData = SingleLiveEvent<Resource<StatusDTO>>()
    val data = MutableLiveData<Resource<List<WalletDTO>>>()

    fun submitRequest(request: AfrimoneyActivateBundleRequest) {
        ResourceUseCaseExecutor(
            afrimoneyActivateBundleUseCase,
            request,
            requestData,
            appExceptionFactory, appSessionNavigator
        ).execute()
    }

    fun getData() {
        ResourceUseCaseExecutor(
            getWalletUseCase,
            Unit,
            data,
            appExceptionFactory,
            appSessionNavigator
        ) {
            getData()
        }.execute()
    }

    override fun onCleared() {
        afrimoneyActivateBundleUseCase.dispose()
        getWalletUseCase.dispose()
        super.onCleared()
    }
}
