package com.tedmob.afrimoney.features.withdraw.agentcode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.app.handleInvalidSession
import com.tedmob.afrimoney.data.*
import com.tedmob.afrimoney.data.entity.SubmitResult
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.data.entity.GetFeesData
import com.tedmob.afrimoney.features.withdraw.agentcode.domain.GetFeesAgentCodeUseCase
import com.tedmob.afrimoney.features.withdraw.agentcode.domain.SubmitAgentCodeUseCase
import com.tedmob.afrimoney.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AgentCodeViewModel
@Inject constructor(
    private val feesUseCase: GetFeesAgentCodeUseCase,
    private val confirmationUseCase: SubmitAgentCodeUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator,

    ) : BaseViewModel() {


    private lateinit var amount: String
    private lateinit var wallet: String

    val data: LiveData<Resource<GetFeesData>> get() = _data
    private val _data = SingleLiveEvent<Resource<GetFeesData>>()

    var feesData: GetFeesData? = null

    val submitted: LiveData<Resource<SubmitResult>> get() = _submitted
    private val _submitted = SingleLiveEvent<Resource<SubmitResult>>()


    data class Params(val agentCode: String, val amount: String)

    fun getFees(code: String, amount: String, wallet: String) {
        execute(
            feesUseCase,
            GetFeesAgentCodeUseCase.Params(code, amount,wallet),
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
            SubmitAgentCodeUseCase.Params(code, amount, pin,wallet),
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