package com.tedmob.africell.features.authentication

import androidx.lifecycle.MutableLiveData
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.SingleLiveEvent


import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.authentication.domain.RegisterUseCase

import com.tedmob.africell.ui.BaseViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class RegisterViewModel
@Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val appExceptionFactory: AppExceptionFactory
) : BaseViewModel() {
    companion object {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    }

    var firstName: String?=null
    var lastName: String? = null
    var mobileNumber: String? = null
    var companyName: String? = null
    var image: String? = null
    var email: String ?=null
    var dobData = MutableLiveData<Long>()
    var password: String ?=null
    var confirmPassword: String ?=null

    val updatedProfileData = SingleLiveEvent<Resource<Unit>>()




    fun setProfile() {
        val params = RegisterUseCase.Params(firstName, lastName,mobileNumber, companyName, email, password, confirmPassword)
        ResourceUseCaseExecutor(registerUseCase, params, updatedProfileData, appExceptionFactory, null).execute()
    }


    override fun onCleared() {
        registerUseCase.dispose()
        super.onCleared()
    }
}