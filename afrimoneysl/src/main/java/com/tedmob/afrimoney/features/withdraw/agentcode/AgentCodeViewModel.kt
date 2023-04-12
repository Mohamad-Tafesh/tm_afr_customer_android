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

    val data: LiveData<Resource<GetFeesData>> get() = _data
    private val _data = MutableLiveData<Resource<GetFeesData>>()


    val submitted: LiveData<Resource<SubmitResult>> get() = _submitted
    private val _submitted = SingleLiveEvent<Resource<SubmitResult>>()


data class Params(val agentCode:String,val amount: String)

    fun getFees(code: String, amount: String) {
        execute(
            feesUseCase,
            GetFeesAgentCodeUseCase.Params(code, amount),
            onLoading = {
                _data.emitLoading()
            },
            onSuccess = {

                this.amount=it.amount
                val data =
                    GetFeesData(
                        it.number,
                        it.amount,
                        it.fees,
                        it.name,
                        it.total
                    )


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

    fun getConfirmation(code:String,pin:String) {

        execute(
            confirmationUseCase,
            SubmitAgentCodeUseCase.Params(code, amount,pin),
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