package com.tedmob.afrimoney.features.transfer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.app.handleInvalidSession
import com.tedmob.afrimoney.data.*
import com.tedmob.afrimoney.data.entity.GetFeesData
import com.tedmob.afrimoney.data.entity.SubmitResult
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.features.transfer.domain.GetFeesUseCase
import com.tedmob.afrimoney.features.transfer.domain.SubmitTransferMoneyDataUseCase
import com.tedmob.afrimoney.ui.BaseViewModel
import com.tedmob.afrimoney.util.security.StringEncryptor
import com.tedmob.afrimoney.util.security.encrypted
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class TransferMoneyViewModel
@Inject constructor(
    private val datas: GetFeesUseCase,
    private val confirmationUseCase: SubmitTransferMoneyDataUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator,
    @Named("local-string") private val encryptor: StringEncryptor,
) : BaseViewModel() {

    var number: String? = null
    var amount: String by encrypted(encryptor, "")
    var feesData: GetFeesData? = null
    var type: Int? = null

    val proceedToConfirm: LiveData<Resource<Unit>> get() = _proceedToConfirm
    private val _proceedToConfirm = SingleLiveEvent<Resource<Unit>>()

    val data: LiveData<Resource<GetFeesData>> get() = _data
    private val _data = SingleLiveEvent<Resource<GetFeesData>>()


    val submitted: LiveData<Resource<SubmitResult>> get() = _submitted
    private val _submitted = SingleLiveEvent<Resource<SubmitResult>>()


    fun proceed(number: String?, amount: Double) {
        this.number = number
        this.amount = amount.toBigDecimal().toPlainString()

        _proceedToConfirm.emitSuccess(Unit)
    }

    fun getFees(number: String?, amount: String, type: Int) {
        execute(
            datas,
            GetFeesUseCase.Params(number, amount),
            onLoading = {
                _data.emitLoading()
            },
            onSuccess = {
                val data =
                    GetFeesData(
                        it.number,
                        it.amount,
                        it.fees,
                        it.name,
                        it.total
                    )

                this.amount = it.amount
                this.type = type

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

    fun getConfirmation(pin: String) {

        execute(
            confirmationUseCase,
            SubmitTransferMoneyDataUseCase.Params(number, amount, pin),
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