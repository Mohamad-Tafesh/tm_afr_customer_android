package com.tedmob.afrimoney.features.bills.powergen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.app.handleInvalidSession
import com.tedmob.afrimoney.data.*
import com.tedmob.afrimoney.data.entity.SubmitResult
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.features.bills.dstv.domain.SubmitCheckDSTVDataUseCase
import com.tedmob.afrimoney.features.bills.powergen.domain.SubmitpowergenDataUseCase
import com.tedmob.afrimoney.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PowergenViewModel
@Inject constructor(

    private val confirmationUseCase: SubmitpowergenDataUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator,

    ) : BaseViewModel() {

    private var amount:String? = null
    private var accnumb:String? = null

    val proceedToConfirm: LiveData<Resource<Unit>> get() = _proceedToConfirm
    private val _proceedToConfirm = SingleLiveEvent<Resource<Unit>>()

    val data: LiveData<Resource<powergenData>> get() = _data
    private val _data = MutableLiveData<Resource<powergenData>>()

    val submitted: LiveData<Resource<SubmitResult>> get() = _submitted
    private val _submitted = SingleLiveEvent<Resource<SubmitResult>>()


    fun proceed(accNumber:String,amount:String) {

        _proceedToConfirm.emitSuccess(Unit)
        this.amount=amount
        this.accnumb=accNumber
        _data.emitSuccess(powergenData(accNumber,amount))
    }

    data class powergenData(val accnumb:String,val amount:String)


    fun getConfirmation(pin:String) {

        execute(
            confirmationUseCase,
            SubmitpowergenDataUseCase.Params(accnumb.orEmpty(), amount.orEmpty(),pin),
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