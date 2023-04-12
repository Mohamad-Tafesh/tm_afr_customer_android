package com.tedmob.afrimoney.features.bills.waec

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.app.handleInvalidSession
import com.tedmob.afrimoney.data.*
import com.tedmob.afrimoney.data.entity.SubmitResult
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.features.bills.waec.domain.GetFeesCheckWaecUseCase
import com.tedmob.afrimoney.features.bills.waec.domain.SubmitWaecDataUseCase
import com.tedmob.afrimoney.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WaecViewModel
@Inject constructor(
    private val feesUseCase: GetFeesCheckWaecUseCase,
    private val confirmationUseCase: SubmitWaecDataUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator,

    ) : BaseViewModel() {


    val proceedToConfirm: LiveData<Resource<String>> get() = _proceedToConfirm
    private val _proceedToConfirm = SingleLiveEvent<Resource<String>>()

    val data: LiveData<Resource<String>> get() = _data
    private val _data = MutableLiveData<Resource<String>>()


    val submitted: LiveData<Resource<SubmitResult>> get() = _submitted
    private val _submitted = SingleLiveEvent<Resource<SubmitResult>>()


    fun getFees(type: String) {
        execute(
            feesUseCase,
            type,
            onLoading = {
                _data.emitLoading()
            },
            onSuccess = {

                _data.emitSuccess(it)
            },
            onError = {
                val exception = appExceptionFactory.make(it)
                if (!exception.handleInvalidSession(appSessionNavigator)) {
                    _data.emitError(exception.userMessage)
                }
            }
        )
    }

    fun getConfirmation(pin: String,amount:String,type:String) {

        execute(
            confirmationUseCase,
            SubmitWaecDataUseCase.Params(pin,amount,type),
            onLoading = {
                _submitted.emitLoading()
            },
            onSuccess = {
                //val result=it.message
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