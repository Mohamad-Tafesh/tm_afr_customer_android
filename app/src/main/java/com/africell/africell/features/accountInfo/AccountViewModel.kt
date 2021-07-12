package com.africell.africell.features.accountInfo

import androidx.lifecycle.MutableLiveData
import com.africell.africell.app.AppSessionNavigator
import com.africell.africell.app.ResourceUseCaseExecutor
import com.africell.africell.data.Resource
import com.africell.africell.data.SingleLiveEvent
import com.africell.africell.data.api.dto.AccountBalanceDTO
import com.africell.africell.data.entity.SubAccount
import com.africell.africell.exception.AppExceptionFactory
import com.africell.africell.features.accountInfo.domain.GetAccountInfoUseCase
import com.africell.africell.features.accountInfo.domain.GetSubAccountUseCase
import com.africell.africell.features.accountsNumber.domain.DeleteAccountUseCase
import com.africell.africell.ui.BaseViewModel
import javax.inject.Inject

class AccountViewModel
@Inject constructor(
    private val getSubAccountUseCase: GetSubAccountUseCase,
    private val getAccountInfoUseCase: GetAccountInfoUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
        private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {

    val accountInfoData = MutableLiveData<Resource<AccountBalanceDTO>>()
    val subAccountData = MutableLiveData<Resource<List<SubAccount>>>()
    val deleteAccountData = SingleLiveEvent<Resource<List<SubAccount>>>()

    fun getSubAccounts() {
        ResourceUseCaseExecutor(getSubAccountUseCase, Unit, subAccountData,appExceptionFactory,appSessionNavigator) {
            getSubAccounts()
        }.execute()
    }


    fun getAccountInfo() {
        ResourceUseCaseExecutor(getAccountInfoUseCase, "", accountInfoData,appExceptionFactory, appSessionNavigator) {
            getAccountInfo()
        }.execute()
    }

    fun deleteSubAccount(msisdn: String) {
        ResourceUseCaseExecutor(deleteAccountUseCase, msisdn, deleteAccountData,appExceptionFactory, appSessionNavigator).execute()

    }

    override fun onCleared() {
        getAccountInfoUseCase.dispose()
        getSubAccountUseCase.dispose()
        super.onCleared()
    }
}
