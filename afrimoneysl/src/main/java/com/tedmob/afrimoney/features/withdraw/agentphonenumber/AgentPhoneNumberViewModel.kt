package com.tedmob.afrimoney.features.withdraw.agentphonenumber

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.app.handleInvalidSession
import com.tedmob.afrimoney.data.*
import com.tedmob.afrimoney.data.entity.SubmitResult
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.data.entity.GetFeesData
import com.tedmob.afrimoney.features.withdraw.agentphonenumber.domain.GetFeesAgentPhoneNumberUseCase
import com.tedmob.afrimoney.features.withdraw.agentphonenumber.domain.SubmitAgentPhoneNumberUseCase
import com.tedmob.afrimoney.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AgentPhoneNumberViewModel
@Inject constructor(
    private val feesUseCase: GetFeesAgentPhoneNumberUseCase,
    private val confirmationUseCase: SubmitAgentPhoneNumberUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator,

    ) : BaseViewModel() {


    val proceedToConfirm: LiveData<Resource<Unit>> get() = _proceedToConfirm
    private val _proceedToConfirm = SingleLiveEvent<Resource<Unit>>()


    val data: LiveData<Resource<GetFeesData>> get() = _data
    private val _data = SingleLiveEvent<Resource<GetFeesData>>()


    val submitted: LiveData<Resource<SubmitResult>> get() = _submitted
    private val _submitted = SingleLiveEvent<Resource<SubmitResult>>()


    var feesData: GetFeesData? = null

    private lateinit var amount: String
    private lateinit var wallet: String

    data class Params(val agentCode: String, val amount: String)

    fun getFees(number: String, amount: String,wallet:String) {
        execute(
            feesUseCase,
            GetFeesAgentPhoneNumberUseCase.Params(number, amount,wallet),
            onLoading = {
                _data.emitLoading()
            },
            onSuccess = {
                this.amount = it.amount
                this.wallet = wallet
                val data =
                    GetFeesData(
                        it.number,
                        it.amount,
                        it.fees,
                        it.name,
                        it.total
                    )


                feesData = data

                _data.emitSuccess(data)
            },
            onError = {
                val exception = appExceptionFactory.make(it)
                if (!exception.handleInvalidSession(appSessionNavigator)) {
                    _data.emitError(exception.userMessage)
                }
            }
        )
    }


    fun getConfirmation(code: String, pin: String) {

        execute(
            confirmationUseCase,
            SubmitAgentPhoneNumberUseCase.Params(code, amount, pin,wallet),
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