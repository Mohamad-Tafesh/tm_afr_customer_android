package com.tedmob.afrimoney.features.banking

import androidx.lifecycle.LiveData
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.data.Resource
import com.tedmob.afrimoney.data.SingleLiveEvent
import com.tedmob.afrimoney.data.entity.Bank
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.features.banking.domain.GetBanksUseCase
import com.tedmob.afrimoney.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BankingServicesViewModel
@Inject constructor(
    private val getBanks: GetBanksUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator,
) : BaseViewModel() {

    val banks: LiveData<Resource<List<Bank>>> get() = _banks
    private val _banks = SingleLiveEvent<Resource<List<Bank>>>()


    fun getBanks() {
        executeResource(
            getBanks,
            Unit,
            _banks,
            appExceptionFactory,
            appSessionNavigator,
            { getBanks() }
        )
    }


    override fun onCleared() {
        super.onCleared()
        //...
    }
}