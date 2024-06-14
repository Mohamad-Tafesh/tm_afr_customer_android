package com.tedmob.afrimoney.features.account

import androidx.lifecycle.LiveData
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.data.Resource
import com.tedmob.afrimoney.data.SingleLiveEvent
import com.tedmob.afrimoney.data.entity.LastTransaction
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.features.account.domain.GetLastTransactionsUseCase
import com.tedmob.afrimoney.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class Last5TransactionsViewModel
@Inject constructor(
    private val getLastTransactions: GetLastTransactionsUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator,
) : BaseViewModel() {

    val transactions: LiveData<Resource<List<LastTransaction>>> get() = _transactions
    private val _transactions = SingleLiveEvent<Resource<List<LastTransaction>>>()


    fun getLastTransactions(pin:String) {
        executeResource(
            getLastTransactions,
            GetLastTransactionsUseCase.Params("30",pin),
            _transactions,
            appExceptionFactory,
            appSessionNavigator,
            action = { getLastTransactions(pin) },
        )
    }


    override fun onCleared() {
        super.onCleared()
        //...
    }
}