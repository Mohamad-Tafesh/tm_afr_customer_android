package com.africell.africell.features.afrimoney

import androidx.lifecycle.MutableLiveData
import com.africell.africell.app.AppSessionNavigator
import com.africell.africell.app.DefaultObserver
import com.africell.africell.app.ResourceUseCaseExecutor
import com.tedmob.afrimoney.data.Resource
import com.africell.africell.data.api.dto.AccountBalanceDTO
import com.africell.africell.data.api.dto.MoneyTransferBalanceDTO
import com.africell.africell.data.entity.SubAccount
import com.africell.africell.data.repository.domain.SessionRepository
import com.africell.africell.exception.AppException
import com.africell.africell.exception.AppExceptionFactory
import com.africell.africell.features.accountInfo.domain.GetAccountInfoUseCase
import com.africell.africell.features.accountInfo.domain.GetSubAccountUseCase
import com.africell.africell.features.afrimoney.domain.GetAfrimoneyBalanceUseCase
import com.africell.africell.features.home.domain.GetImagesUseCase
import com.africell.africell.features.home.domain.SetUserPushUseCase
import com.africell.africell.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AfrimoneyViewModel
@Inject constructor(
    private val sessionRepository: SessionRepository,
    private val getAfrimoneyBalanceUseCase: GetAfrimoneyBalanceUseCase,
    private val getSubAccountUseCase: GetSubAccountUseCase,
    private val getImagesUseCase: GetImagesUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {


    /*fun getProducts(){
             ResourceUseCaseExecutor(getProductsUseCase, Unit, productsData,appExceptionFactory, appSessionNavigator) {
                 getProducts()
             }.execute()
     }*/
    val subAccountData = MutableLiveData<Resource<List<SubAccount>>>()
    val imagesData = MutableLiveData<Resource<List<String>>>()

    val accountInfoData = MutableLiveData<Resource<List<MoneyTransferBalanceDTO>>>()

    fun getSubAccounts() {
        ResourceUseCaseExecutor(getSubAccountUseCase, Unit, subAccountData, appExceptionFactory, appSessionNavigator) {
            getSubAccounts()
        }.execute()
    }

    fun getAccountInfo() {
        ResourceUseCaseExecutor(getAfrimoneyBalanceUseCase, "", accountInfoData,appExceptionFactory, appSessionNavigator) {
            getAccountInfo()
        }.execute()
    }

    fun getImages(imageType: String?, pageName: String?) {
        ResourceUseCaseExecutor(
            getImagesUseCase,
            GetImagesUseCase.Params(imageType, pageName),
            imagesData,
            appExceptionFactory,
            appSessionNavigator
        ).execute()
    }

    override fun onCleared() {
        super.onCleared()
        getSubAccountUseCase.dispose()

    }
}