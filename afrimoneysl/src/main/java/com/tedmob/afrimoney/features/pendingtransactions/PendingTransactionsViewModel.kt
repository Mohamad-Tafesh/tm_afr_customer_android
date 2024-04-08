package com.tedmob.afrimoney.features.pendingtransactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.app.handleInvalidSession
import com.tedmob.afrimoney.data.*
import com.tedmob.afrimoney.data.api.dto.PendingTransactionsData
import com.tedmob.afrimoney.data.entity.SubmitResult
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.features.pendingtransactions.domain.GetPendingTransactionsUseCase
import com.tedmob.afrimoney.features.pendingtransactions.domain.SubmitPendingTransactionUseCase
import com.tedmob.afrimoney.ui.BaseViewModel


import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PendingTransactionsViewModel
@Inject constructor(
    private val confirmationUseCase: SubmitPendingTransactionUseCase,
    private val getTransactionsUseCase: GetPendingTransactionsUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator,
) : BaseViewModel() {

    val transactionData: LiveData<Resource<List<PendingTransactionsData>>> get() = _transactionData
    private val _transactionData = MutableLiveData<Resource<List<PendingTransactionsData>>>()


    val submitted: LiveData<Resource<SubmitResult>> get() = _submitted
    private val _submitted = SingleLiveEvent<Resource<SubmitResult>>()



    fun getData(pin: String, service: String) {
        executeResource(
            getTransactionsUseCase,
            GetPendingTransactionsUseCase.Params(pin, service),
            _transactionData,
            appExceptionFactory,
            appSessionNavigator
        )
    }

    fun getConfirmation(type:String,pin:String,SvId:String) {

        execute(
            confirmationUseCase,
            SubmitPendingTransactionUseCase.Params(type,pin, SvId),
            onLoading = {
                _submitted.emitLoading()
            },
            onSuccess = {

                _submitted.emitSuccess(it)
            },
            onError = {
                val exception = appExceptionFactory.make(it)
                if (!exception.handleInvalidSession(appSessionNavigator)) {
                    _submitted.emitError(exception.userMessage)
                }
            }
        )
    }


    override fun onCleared() {
        super.onCleared()
    }
}
