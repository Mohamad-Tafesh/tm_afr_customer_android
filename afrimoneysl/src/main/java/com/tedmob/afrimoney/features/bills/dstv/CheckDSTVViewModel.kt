package com.tedmob.afrimoney.features.bills.dstv

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.app.handleInvalidSession
import com.tedmob.afrimoney.data.*
import com.tedmob.afrimoney.data.entity.SubmitResult
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.features.bills.dstv.domain.SubmitCheckDSTVDataUseCase
import com.tedmob.afrimoney.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CheckDSTVViewModel
@Inject constructor(

    private val confirmationUseCase: SubmitCheckDSTVDataUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator,

    ) : BaseViewModel() {


    val proceedToConfirm: LiveData<Resource<String>> get() = _proceedToConfirm
    private val _proceedToConfirm = SingleLiveEvent<Resource<String>>()

    val data: LiveData<Resource<String>> get() = _data
    private val _data = MutableLiveData<Resource<String>>()

    val submitted: LiveData<Resource<SubmitResult>> get() = _submitted
    private val _submitted = SingleLiveEvent<Resource<SubmitResult>>()


    fun proceed(cardNumber: String) {
        _data.emitSuccess(cardNumber)
        _proceedToConfirm.emitSuccess(cardNumber)
    }


    fun getConfirmation(cardNumber: String) {

        execute(
            confirmationUseCase,
            cardNumber,
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

}