package com.tedmob.afrimoney.features.bills.mercury

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.app.handleInvalidSession
import com.tedmob.afrimoney.data.*
import com.tedmob.afrimoney.data.entity.SubmitResult
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.features.bills.mercury.domain.SubmitMercuryUseCase
import com.tedmob.afrimoney.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MercuryViewModel
@Inject constructor(

    private val confirmationUseCase: SubmitMercuryUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator,

    ) : BaseViewModel() {

    private var amount:String? = null
    private var number:String? = null

    val proceedToConfirm: LiveData<Resource<Unit>> get() = _proceedToConfirm
    private val _proceedToConfirm = SingleLiveEvent<Resource<Unit>>()

    val data: LiveData<Resource<MercuryData>> get() = _data
    private val _data = MutableLiveData<Resource<MercuryData>>()

    val submitted: LiveData<Resource<SubmitResult>> get() = _submitted
    private val _submitted = SingleLiveEvent<Resource<SubmitResult>>()


    fun proceed(number:String,amount:String) {

        _proceedToConfirm.emitSuccess(Unit)
        this.amount=amount
        this.number=number
        _data.emitSuccess(MercuryData(number,amount))
    }

    data class MercuryData(val number:String,val amount:String)


    fun getConfirmation(pin:String) {

        execute(
            confirmationUseCase,
            SubmitMercuryUseCase.Params(number.orEmpty(), amount.orEmpty(),pin),
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