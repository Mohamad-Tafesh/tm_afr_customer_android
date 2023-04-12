package com.tedmob.afrimoney.features.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.data.Resource
import com.tedmob.afrimoney.data.SingleLiveEvent
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.features.account.domain.GetUserAccountInfoUseCase
import com.tedmob.afrimoney.features.account.domain.LogoutUseCase
import com.tedmob.afrimoney.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel
@Inject constructor(
    private val getUserAccountInfoUseCase: GetUserAccountInfoUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator,
) : BaseViewModel() {

    val info: LiveData<Resource<UserAccountInfo>> get() = _info
    private val _info = MutableLiveData<Resource<UserAccountInfo>>()

    val loggedOut: LiveData<Resource<Unit>> get() = _loggedOut
    private val _loggedOut = SingleLiveEvent<Resource<Unit>>()


    fun getUserInfo() {
        executeResource(
            getUserAccountInfoUseCase,
            Unit,
            _info,
            appExceptionFactory,
            appSessionNavigator,
            action = { getUserInfo() },
        )
    }

    fun logout() {
        executeResource(
            logoutUseCase,
            Unit,
            _loggedOut,
            appExceptionFactory,
            appSessionNavigator,
        )
    }


    override fun onCleared() {
        super.onCleared()
        //...
    }
}