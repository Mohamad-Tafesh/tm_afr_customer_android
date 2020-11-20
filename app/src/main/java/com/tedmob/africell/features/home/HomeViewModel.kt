package com.tedmob.africell.features.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tedmob.africell.data.repository.domain.SessionRepository
import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.authentication.domain.SetUserPushUseCase
import com.tedmob.africell.app.DefaultObserver
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource
import com.tedmob.africell.exception.AppException
import com.tedmob.africell.ui.BaseViewModel
import javax.inject.Inject


class HomeViewModel
@Inject constructor(
    private val setUserPushUseCase: SetUserPushUseCase,
    private val sessionRepository: SessionRepository,
    private val appExceptionFactory: AppExceptionFactory
) : BaseViewModel() {


   /*fun getProducts(){
            ResourceUseCaseExecutor(getProductsUseCase, Unit, productsData, appExceptionFactory) {
                getProducts()
            }.execute()
    }*/

    fun setUserPush() {
        if (sessionRepository.isLoggedIn()) {
            setUserPushUseCase.execute(Unit, object : DefaultObserver<Unit>(appExceptionFactory) {
                override fun onNext(t: Unit) {
                }

                override fun onError(e: AppException) {
                }
            })
        }
    }



    override fun onCleared() {
        super.onCleared()
        }
}