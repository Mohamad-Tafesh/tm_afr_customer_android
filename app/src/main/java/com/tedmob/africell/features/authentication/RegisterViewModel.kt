package com.tedmob.africell.features.authentication

import androidx.lifecycle.MutableLiveData
import com.tedmob.africell.app.AppSessionNavigator
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.SingleLiveEvent


import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.authentication.domain.RegisterUseCase

import com.tedmob.africell.ui.BaseViewModel
import com.tedmob.africell.util.ISO_DATE_FORMAT
import com.tedmob.africell.util.toFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class RegisterViewModel
@Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {
    companion object {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    }


    val updatedProfileData = SingleLiveEvent<Resource<Unit>>()

   val  dobData = MutableLiveData<Long>()


    fun setProfile( firstName: String?, lastName: String?, email: String ?,  password: String ?, confirmPassword: String ?
    ) {
        val params = RegisterUseCase.Params(firstName, lastName, email,dobData.value?.toFormat(ISO_DATE_FORMAT), password, confirmPassword)
        ResourceUseCaseExecutor(registerUseCase, params, updatedProfileData,appExceptionFactory, appSessionNavigator,null).execute()
    }


    override fun onCleared() {
        registerUseCase.dispose()
        super.onCleared()
    }
}