package com.africell.africell.features.authentication

import androidx.lifecycle.MutableLiveData
import com.africell.africell.app.AppSessionNavigator
import com.africell.africell.app.ResourceUseCaseExecutor
import com.tedmob.afrimoney.data.Resource
import com.africell.africell.data.SingleLiveEvent


import com.africell.africell.exception.AppExceptionFactory
import com.africell.africell.features.authentication.domain.RegisterUseCase

import com.africell.africell.ui.BaseViewModel
import com.africell.africell.util.ISO_DATE_FORMAT
import com.africell.africell.util.toFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@HiltViewModel
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