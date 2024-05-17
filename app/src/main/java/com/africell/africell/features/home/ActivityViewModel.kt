package com.africell.africell.features.home

import androidx.lifecycle.LiveData
import com.africell.africell.app.ResourceUseCaseExecutor
import com.africell.africell.features.settings.domain.DeleteAccountUseCase
import com.africell.africell.features.settings.domain.LogoutUseCase
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.data.Resource
import com.tedmob.afrimoney.data.SingleLiveEvent
import com.tedmob.afrimoney.data.entity.UserState
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.features.newhome.domain.VerifyUseCase
import com.tedmob.afrimoney.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel
@Inject constructor(
    private val verifyUseCase: VerifyUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator,
    private val logoutUseCase: LogoutUseCase,
    private val appExceptionFactorys: com.africell.africell.exception.AppExceptionFactory,
    private val appSessionNavigators: com.africell.africell.app.AppSessionNavigator

) : BaseViewModel() {

    val verified: LiveData<Resource<UserState>> get() = _verified
    private val _verified = SingleLiveEvent<Resource<UserState>>()


    fun verify(mobilenb: String) {
        executeResource(
            verifyUseCase,
            mobilenb,
            _verified,
            appExceptionFactory,
            null,
            null
        )
    }

    val logoutData = com.africell.africell.data.SingleLiveEvent<Resource<Unit>>()

    fun logout() {
        ResourceUseCaseExecutor(logoutUseCase, Unit, logoutData, appExceptionFactorys,appSessionNavigators).execute()
    }


    override fun onCleared() {
        super.onCleared()
    }
}