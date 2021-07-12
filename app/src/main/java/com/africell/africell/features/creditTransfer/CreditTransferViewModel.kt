package com.africell.africell.features.creditTransfer

import androidx.lifecycle.MutableLiveData
import com.africell.africell.app.AppSessionNavigator
import com.africell.africell.app.ResourceUseCaseExecutor
import com.africell.africell.data.Resource
import com.africell.africell.data.SingleLiveEvent
import com.africell.africell.data.api.dto.SMSCountDTO
import com.africell.africell.data.entity.Country
import com.africell.africell.data.entity.SubAccount
import com.africell.africell.exception.AppExceptionFactory
import com.africell.africell.features.accountInfo.domain.GetSubAccountUseCase
import com.africell.africell.features.authentication.domain.GetCountriesUseCase
import com.africell.africell.features.creditTransfer.domain.CreditTransferUseCase
import com.africell.africell.ui.BaseViewModel
import javax.inject.Inject

class CreditTransferViewModel
@Inject constructor(
    private val creditTransferUseCase: CreditTransferUseCase,
    private val getCountries: GetCountriesUseCase,
    private val getSubAccountUseCase: GetSubAccountUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {


    val creditTransferData = SingleLiveEvent<Resource<SMSCountDTO>>()
    val countriesData = MutableLiveData<Resource<List<Country>>>()
    val subAccountData = MutableLiveData<Resource<List<SubAccount>>>()

    fun creditTransfer(senderMsisdn: String?,receiverMsisdn: String?, voucher: String) {
        ResourceUseCaseExecutor(
            creditTransferUseCase,
            CreditTransferUseCase.Params(senderMsisdn,receiverMsisdn, voucher),
            creditTransferData,
            appExceptionFactory,appSessionNavigator
        ).execute()
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
        creditTransferUseCase.dispose()
        getSubAccountUseCase.dispose()
        super.onCleared()
    }
}
