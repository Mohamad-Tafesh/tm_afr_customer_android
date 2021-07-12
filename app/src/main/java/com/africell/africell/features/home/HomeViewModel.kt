package com.africell.africell.features.home

import androidx.lifecycle.MutableLiveData
import com.africell.africell.app.AppSessionNavigator
import com.africell.africell.app.DefaultObserver
import com.africell.africell.app.ResourceUseCaseExecutor
import com.africell.africell.data.Resource
import com.africell.africell.data.entity.SubAccount
import com.africell.africell.data.repository.domain.SessionRepository
import com.africell.africell.exception.AppException
import com.africell.africell.exception.AppExceptionFactory
import com.africell.africell.features.accountInfo.domain.GetSubAccountUseCase
import com.africell.africell.features.home.domain.GetImagesUseCase
import com.africell.africell.features.home.domain.SetUserPushUseCase
import com.africell.africell.ui.BaseViewModel
import javax.inject.Inject


class HomeViewModel
@Inject constructor(
    private val setUserPushUseCase: SetUserPushUseCase,
    private val sessionRepository: SessionRepository,
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
    fun getSubAccounts() {
        ResourceUseCaseExecutor(getSubAccountUseCase, Unit, subAccountData, appExceptionFactory, appSessionNavigator) {
            getSubAccounts()
        }.execute()
    }

    fun setUserPush() {
        if (sessionRepository.isLoggedIn()) {
            setUserPushUseCase.execute(Unit, object : DefaultObserver<Unit>(appExceptionFactory,appSessionNavigator) {
                override fun onNext(t: Unit) {
                }

                override fun onError(e: AppException) {
                }
            })
        }
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