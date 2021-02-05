package com.tedmob.africell.features.accountInfo

import androidx.lifecycle.MutableLiveData
import com.tedmob.africell.app.AppSessionNavigator
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.api.dto.AccountBalanceDTO
import com.tedmob.africell.data.entity.SubAccount
import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.accountInfo.domain.GetAccountInfoUseCase
import com.tedmob.africell.features.accountInfo.domain.GetSubAccountUseCase
import com.tedmob.africell.features.accountsNumber.domain.DeleteAccountUseCase
import com.tedmob.africell.ui.BaseViewModel
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
    val deleteAccountData = MutableLiveData<Resource<List<SubAccount>>>()

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
