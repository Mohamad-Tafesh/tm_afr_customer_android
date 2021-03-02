package com.tedmob.africell.features.settings

import com.tedmob.africell.app.AppSessionNavigator
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.SingleLiveEvent
import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.settings.domain.LogoutUseCase
import com.tedmob.africell.ui.BaseViewModel

import javax.inject.Inject


class SettingsViewModel
@Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {


   val logoutData = SingleLiveEvent<Resource<Unit>>()


    fun logout() {
        ResourceUseCaseExecutor(logoutUseCase, Unit, logoutData, appExceptionFactory,appSessionNavigator).execute()
    }


    override fun onCleared() {
        super.onCleared()
        logoutUseCase.dispose()
    }
}