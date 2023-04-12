package com.africell.africell.features.afrimoneyLineRecharge

import androidx.lifecycle.MutableLiveData
import com.africell.africell.app.AppSessionNavigator
import com.africell.africell.app.ResourceUseCaseExecutor
import com.tedmob.afrimoney.data.Resource
import com.africell.africell.data.SingleLiveEvent
import com.africell.africell.data.api.dto.StatusDTO
import com.africell.africell.data.api.dto.WalletDTO
import com.africell.africell.data.api.requests.afrimoney.AirlineRequest
import com.africell.africell.exception.AppExceptionFactory
import com.africell.africell.features.afrimoney.domain.GetWalletUseCase
import com.africell.africell.features.afrimoneyLineRecharge.domain.LineRechargeRequestUseCase
import com.africell.africell.features.authentication.domain.GetCountriesUseCase
import com.africell.africell.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AfrimoneyLineRechargeViewModel
@Inject constructor(
    private val lineRechargeRequestUseCase: LineRechargeRequestUseCase,
    private val getWalletUseCase: GetWalletUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {


    val requestData = SingleLiveEvent<Resource<StatusDTO>>()
    val data = MutableLiveData<Resource<List<WalletDTO>>>()

    fun submitRequest(request: AirlineRequest) {
        ResourceUseCaseExecutor(
            lineRechargeRequestUseCase,
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
        lineRechargeRequestUseCase.dispose()
        getWalletUseCase.dispose()
        super.onCleared()
    }
}
