package com.africell.africell.features.authentication

import com.africell.africell.app.AppSessionNavigator
import com.africell.africell.app.ResourceUseCaseExecutor
import com.africell.africell.data.Resource
import com.africell.africell.data.SingleLiveEvent


import com.africell.africell.exception.AppExceptionFactory
import com.africell.africell.features.authentication.domain.ResetPasswordUseCase

import com.africell.africell.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
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