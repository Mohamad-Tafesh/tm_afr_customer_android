package com.tedmob.afrimoney.features.bills.dstv

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.app.handleInvalidSession
import com.tedmob.afrimoney.data.*
import com.tedmob.afrimoney.data.entity.Press
import com.tedmob.afrimoney.data.entity.SubmitResult
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.features.bills.dstv.domain.GetPressUseCase
import com.tedmob.afrimoney.features.bills.dstv.domain.ProceedRenewUseCase
import com.tedmob.afrimoney.features.bills.dstv.domain.SubmitRenewDSTVDataUseCase
import com.tedmob.afrimoney.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RenewDSTVViewModel
@Inject constructor(
    private val dstvUseCase: ProceedRenewUseCase,
    private val pressUseCase: GetPressUseCase,
    private val confirmationUseCase: SubmitRenewDSTVDataUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator,

    ) : BaseViewModel() {


    val proceedToConfirm: LiveData<Resource<Params>> get() = _proceedToConfirm
    private val _proceedToConfirm = SingleLiveEvent<Resource<Params>>()

    val data: LiveData<Resource<List<Press>>> get() = _data
    private val _data = MutableLiveData<Resource<List<Press>>>()


    val submitted: LiveData<Resource<SubmitResult>> get() = _submitted
    private val _submitted = SingleLiveEvent<Resource<SubmitResult>>()


    fun proceed(cardNumber: String, press: String, subType: String, nbOfMonths: String) {

        execute(
            dstvUseCase,
            ProceedRenewUseCase.Params(press, subType, nbOfMonths, cardNumber),
            onLoading = {
                _data.emitLoading()
            },
            onSuccess = {
                _proceedToConfirm.emitSuccess(Params(cardNumber, press, subType, nbOfMonths, it))
            },
            onError = {
                val exception = appExceptionFactory.make(it)
                if (!exception.handleInvalidSession(appSessionNavigator)) {
                    _data.emitError(exception.userMessage)
                }
            }
        )
    }

    data class Params(
        val cardNumber: String, val press: String, val subType: String, val nbOfMonths: String,
        val amount: String
    )

    fun getPress() {

        execute(
            pressUseCase,
            Unit,
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

    fun getConfirmation(
        cardNumber: String,
        months: String,
        amount: String,
        pin: String
    ) {

        execute(
            confirmationUseCase,
            SubmitRenewDSTVDataUseCase.Params(cardNumber, months, amount, pin),
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