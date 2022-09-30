package com.africell.africell.features.settings

import com.africell.africell.app.AppSessionNavigator
import com.africell.africell.app.ResourceUseCaseExecutor
import com.africell.africell.data.Resource
import com.africell.africell.data.SingleLiveEvent
import com.africell.africell.exception.AppExceptionFactory
import com.africell.africell.features.settings.domain.DeleteAccountUseCase
import com.africell.africell.features.settings.domain.LogoutUseCase
import com.africell.africell.ui.BaseViewModel

import javax.inject.Inject


class SettingsViewModel
@Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {


   val logoutData = SingleLiveEvent<Resource<Unit>>()


    fun logout() {
        ResourceUseCaseExecutor(logoutUseCase, Unit, logoutData, appExceptionFactory,appSessionNavigator).execute()
    }

    val deleteAccountData = SingleLiveEvent<Resource<Unit>>()


    fun deleteAccount() {
        ResourceUseCaseExecutor(deleteAccountUseCase, Unit, deleteAccountData, appExceptionFactory,appSessionNavigator).execute()
    }

    override fun onCleared() {
        super.onCleared()
        logoutUseCase.dispose()
        deleteAccountUseCase.dispose()
    }
}