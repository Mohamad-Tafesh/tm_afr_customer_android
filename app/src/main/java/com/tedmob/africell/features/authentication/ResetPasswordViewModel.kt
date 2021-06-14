package com.tedmob.africell.features.authentication

import com.tedmob.africell.app.AppSessionNavigator
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.SingleLiveEvent


import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.authentication.domain.ResetPasswordUseCase

import com.tedmob.africell.ui.BaseViewModel
import javax.inject.Inject


class ResetPasswordViewModel
@Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase,
        private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {

    val resetData = SingleLiveEvent<Resource<Unit>>()

    fun resetPassword(password:String, confirmPassword:String) {
        val params = ResetPasswordUseCase.Params(password, confirmPassword)
        ResourceUseCaseExecutor(resetPasswordUseCase, params, resetData,appExceptionFactory, appSessionNavigator, null).execute()
    }


    override fun onCleared() {
        resetPasswordUseCase.dispose()
        super.onCleared()
    }
}