package com.tedmob.africell.features.profile

import com.tedmob.africell.app.AppSessionNavigator
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.SingleLiveEvent
import com.tedmob.africell.data.api.dto.ChangePasswordDTO


import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.profile.domain.ChangePasswordUseCase

import com.tedmob.africell.ui.BaseViewModel
import javax.inject.Inject


class ChangePasswordViewModel
@Inject constructor(
    private val changePasswordUseCase: ChangePasswordUseCase,
        private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {

    val changePasswordData = SingleLiveEvent<Resource<ChangePasswordDTO>>()

    fun changePassword(oldPassword:String, password:String, confirmPassword:String) {
        val params = ChangePasswordUseCase.Params(oldPassword,password, confirmPassword)
        ResourceUseCaseExecutor(changePasswordUseCase, params, changePasswordData,appExceptionFactory, appSessionNavigator, null).execute()
    }


    override fun onCleared() {
        changePasswordUseCase.dispose()
        super.onCleared()
    }
}