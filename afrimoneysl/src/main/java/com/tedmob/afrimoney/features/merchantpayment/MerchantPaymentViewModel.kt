package com.tedmob.afrimoney.features.merchantpayment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.app.handleInvalidSession
import com.tedmob.afrimoney.data.*
import com.tedmob.afrimoney.data.entity.GetFeesData
import com.tedmob.afrimoney.data.entity.SubmitResult
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.features.merchantpayment.domain.GetFeesUseCase
import com.tedmob.afrimoney.features.merchantpayment.domain.SubmitMerchantPaymentDataUseCase

import com.tedmob.afrimoney.ui.BaseViewModel
import com.tedmob.afrimoney.util.security.StringEncryptor
import com.tedmob.afrimoney.util.security.encrypted
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MerchantPaymentViewModel
@Inject constructor(
    private val datas: GetFeesUseCase,
    private val confirmationUseCase: SubmitMerchantPaymentDataUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator,
    @Named("local-string") private val encryptor: StringEncryptor,
) : BaseViewModel() {

    var feesData: MerchantPaymentData? = null

    private var merchantCode: String by encrypted(encryptor, "")
    private var amount: String by encrypted(encryptor, "")
    public var refId: String by encrypted(encryptor, "")

    val proceedToConfirm: LiveData<Resource<Unit>> get() = _proceedToConfirm
    private val _proceedToConfirm = SingleLiveEvent<Resource<Unit>>()

    val data: LiveData<Resource<MerchantPaymentData>> get() = _data
    private val _data = SingleLiveEvent<Resource<MerchantPaymentData>>()


    val submitted: LiveData<Resource<SubmitResult>> get() = _submitted
    private val _submitted = SingleLiveEvent<Resource<SubmitResult>>()


    fun proceed(merchantCode: String, amount: Double,refId:String) {
        this.merchantCode = merchantCode
        this.amount = amount.toBigDecimal().toPlainString()
        this.refId=refId

        _proceedToConfirm.emitSuccess(Unit)
    }

    fun getFees(msisdn: String, amount: String) {
        execute(
            datas,
            GetFeesUseCase.Params(msisdn, amount),
            onLoading = {
                _data.emitLoading()
            },
            onSuccess = {
                val data =
                    MerchantPaymentData(
                        it.merchantCode,
                        it.amount,
                        it.name,
                        it.fees,
                        it.total
                    )
                feesData = data

                this.amount=it.amount
                this.merchantCode=it.merchantCode


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

    fun getConfirmation(pin:String) {

        execute(
            confirmationUseCase,
            SubmitMerchantPaymentDataUseCase.Params(merchantCode, amount,pin,refId),
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