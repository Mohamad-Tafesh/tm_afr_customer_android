package com.tedmob.africell.features.authentication

import androidx.lifecycle.MutableLiveData
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.SingleLiveEvent


import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.authentication.domain.RegisterUseCase
import com.tedmob.africell.features.authentication.domain.ResetPasswordUseCase

import com.tedmob.africell.ui.BaseViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class ResetPasswordViewModel
@Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val appExceptionFactory: AppExceptionFactory
) : BaseViewModel() {

    val resetData = SingleLiveEvent<Resource<Unit>>()

    fun resetPassword(password:String, confirmPassword:String) {
        val params = ResetPasswordUseCase.Params(password, confirmPassword)
        ResourceUseCaseExecutor(resetPasswordUseCase, params, resetData, appExceptionFactory, null).execute()
    }


    override fun onCleared() {
        resetPasswordUseCase.dispose()
        super.onCleared()
    }
}