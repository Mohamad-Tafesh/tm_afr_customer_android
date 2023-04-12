package com.africell.africell.features.home

import androidx.lifecycle.LiveData
import com.africell.africell.app.AppSessionNavigator
import com.africell.africell.exception.AppExceptionFactory
import com.africell.africell.features.home.domain.VerifyUseCase
import com.africell.africell.ui.BaseViewModel
import com.tedmob.afrimoney.data.Resource
import com.tedmob.afrimoney.data.SingleLiveEvent
import com.tedmob.afrimoney.data.entity.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ActivityViewModel
@Inject constructor(
    private val verifyUseCase: VerifyUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
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

    override fun onCleared() {
        super.onCleared()

    }
}