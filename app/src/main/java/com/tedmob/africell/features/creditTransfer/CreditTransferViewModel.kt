package com.tedmob.africell.features.creditTransfer

import androidx.lifecycle.MutableLiveData
import com.tedmob.africell.app.AppSessionNavigator
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.SingleLiveEvent
import com.tedmob.africell.data.api.dto.SMSCountDTO
import com.tedmob.africell.data.entity.Country
import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.authentication.domain.GetCountriesUseCase
import com.tedmob.africell.features.creditTransfer.domain.CreditTransferUseCase
import com.tedmob.africell.ui.BaseViewModel
import javax.inject.Inject

class CreditTransferViewModel
@Inject constructor(
    private val creditTransferUseCase: CreditTransferUseCase,
    private val getCountries: GetCountriesUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {


    val creditTransferData = SingleLiveEvent<Resource<SMSCountDTO>>()
    val countriesData = MutableLiveData<Resource<List<Country>>>()
    fun creditTransfer(receiverMsisdn: String?, voucher: String) {
        ResourceUseCaseExecutor(
            creditTransferUseCase,
            CreditTransferUseCase.Params(receiverMsisdn, voucher),
            creditTransferData,
            appExceptionFactory,appSessionNavigator
        ).execute()
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
        creditTransferUseCase.dispose()
        super.onCleared()
    }
}
