package com.africell.africell.features.profile

import com.africell.africell.app.AppSessionNavigator
import com.africell.africell.app.ResourceUseCaseExecutor
import com.tedmob.afrimoney.data.Resource
import com.africell.africell.data.SingleLiveEvent
import com.africell.africell.data.api.dto.ChangePasswordDTO


import com.africell.africell.exception.AppExceptionFactory
import com.africell.africell.features.profile.domain.ChangePasswordUseCase

import com.africell.africell.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
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