package com.tedmob.africell.features.accountInfo

import androidx.lifecycle.MutableLiveData
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.api.dto.*
import com.tedmob.africell.data.entity.SubAccount
import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.accountInfo.domain.GetAccountInfoUseCase
import com.tedmob.africell.features.accountInfo.domain.GetSubAccountUseCase

import com.tedmob.africell.ui.BaseViewModel
import javax.inject.Inject

class AccountViewModel
@Inject constructor(
    private val getSubAccountUseCase: GetSubAccountUseCase,
    private val getAccountInfoUseCase: GetAccountInfoUseCase,
    private val appExceptionFactory: AppExceptionFactory
) : BaseViewModel() {

    val accountInfoData = MutableLiveData<Resource<AccountBalanceDTO>>()
    val subAccountData = MutableLiveData<Resource<List<SubAccount>>>()


    fun getSubAccounts() {
        ResourceUseCaseExecutor(getSubAccountUseCase,Unit, subAccountData, appExceptionFactory) {
            getSubAccounts()
        }.execute()
    }


    fun getAccountInfo(msisdn:String) {
        ResourceUseCaseExecutor(getAccountInfoUseCase,msisdn, accountInfoData, appExceptionFactory) {
            getAccountInfo(msisdn)
        }.execute()
    }

    override fun onCleared() {
        getAccountInfoUseCase.dispose()
        getSubAccountUseCase.dispose()
        super.onCleared()
    }
}
