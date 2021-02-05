package com.tedmob.africell.features.home

import androidx.lifecycle.MutableLiveData
import com.tedmob.africell.app.AppSessionNavigator
import com.tedmob.africell.app.DefaultObserver
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.entity.SubAccount
import com.tedmob.africell.data.repository.domain.SessionRepository
import com.tedmob.africell.exception.AppException
import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.accountInfo.domain.GetSubAccountUseCase
import com.tedmob.africell.features.authentication.domain.SetUserPushUseCase
import com.tedmob.africell.features.home.domain.GetImagesUseCase
import com.tedmob.africell.ui.BaseViewModel
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