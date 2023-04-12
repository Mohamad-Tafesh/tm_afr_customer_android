package com.tedmob.afrimoney.features.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.app.usecase.ObservableResourceUseCaseExecutor
import com.tedmob.afrimoney.data.Resource
import com.tedmob.afrimoney.data.api.dto.BalanceDTO
import com.tedmob.afrimoney.data.entity.UserHomeData
import com.tedmob.afrimoney.data.repository.domain.SessionRepository
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.features.account.UserAccountInfo
import com.tedmob.afrimoney.features.home.domain.GetBalanceUseCase
import com.tedmob.afrimoney.features.home.domain.GetHomeDataUseCase
import com.tedmob.afrimoney.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val getHomeUseCase: GetHomeDataUseCase,
    private val getBlanceUseCase: GetBalanceUseCase,
    private val session: SessionRepository,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator,
) : BaseViewModel() {

    val data: LiveData<Resource<UserHomeData>> get() = _data
    private val _data = MutableLiveData<Resource<UserHomeData>>()

    val userName: LiveData<Resource<String>> get() = _username
    private val _username = MutableLiveData<Resource<String>>()

    val balance: LiveData<Resource<UserAccountInfo>> get() = _balance
    private val _balance = MutableLiveData<Resource<UserAccountInfo>>()

    //...


    fun getData() {
        executeResource(
            getHomeUseCase,
            Unit,
            _data,
            appExceptionFactory,
            appSessionNavigator,
            action = { getData() },
        )
    }



    fun getBalance() {
        ObservableResourceUseCaseExecutor(
            getBlanceUseCase,
            "",
            _balance,
            appExceptionFactory,
        ).execute()
    }

    //...


    override fun onCleared() {
        super.onCleared()
        //...
    }
}