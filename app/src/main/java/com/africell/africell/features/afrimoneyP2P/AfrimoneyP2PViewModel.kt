package com.africell.africell.features.afrimoneyP2P

import androidx.lifecycle.MutableLiveData
import com.africell.africell.app.AppSessionNavigator
import com.africell.africell.app.ResourceUseCaseExecutor
import com.africell.africell.data.Resource
import com.africell.africell.data.SingleLiveEvent
import com.africell.africell.data.api.dto.StatusDTO
import com.africell.africell.data.api.dto.WalletDTO
import com.africell.africell.data.api.requests.afrimoney.P2PRequest
import com.africell.africell.exception.AppExceptionFactory
import com.africell.africell.features.afrimoney.domain.GetWalletUseCase
import com.africell.africell.features.afrimoneyP2P.domain.P2PRequestUseCase
import com.africell.africell.features.authentication.domain.GetCountriesUseCase
import com.africell.africell.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AfrimoneyP2PViewModel
@Inject constructor(
    private val p2pRequestUseCase: P2PRequestUseCase,
    private val getCountries: GetCountriesUseCase,
    private val getWalletUseCase: GetWalletUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {


    val requestData = SingleLiveEvent<Resource<StatusDTO>>()
    val data = MutableLiveData<Resource<List<WalletDTO>>>()

    fun submitRequest(request: P2PRequest) {
        ResourceUseCaseExecutor(
            p2pRequestUseCase,
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
        p2pRequestUseCase.dispose()
        getWalletUseCase.dispose()
        super.onCleared()
    }
}
