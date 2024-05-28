package com.tedmob.afrimoney.features.africellservices.airtime

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.app.handleInvalidSession
import com.tedmob.afrimoney.data.*
import com.tedmob.afrimoney.data.entity.Bundlelist
import com.tedmob.afrimoney.data.entity.BundlelistParent
import com.tedmob.afrimoney.data.entity.SubmitResult
import com.tedmob.afrimoney.data.repository.domain.SessionRepository
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.features.africellservices.airtime.domain.GetAirtimeServiceUseCase
import com.tedmob.afrimoney.features.africellservices.databundle.domain.BundleOtherConfirmationUseCase
import com.tedmob.afrimoney.features.africellservices.databundle.domain.BundleSelfConfirmationUseCase
import com.tedmob.afrimoney.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AirtimeViewModel
@Inject constructor(
    private val getAirtimeData:GetAirtimeServiceUseCase,
    private val confirmationselfUseCase: BundleSelfConfirmationUseCase,
    private val confirmationotherUseCase: BundleOtherConfirmationUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val session: SessionRepository,
    private val appSessionNavigator: AppSessionNavigator,

    ) : BaseViewModel() {

    class Params(
        val type:Int,
        val number: String,
        val bundleName: String,
        val remark: String,
        val transactionAmount: String,
        val idValue: String,
        val idType: String,
        val bundle: String,
        val validity:String
        )

    val proceedToConfirm: LiveData<Resource<Unit>> get() = _proceedToConfirm
    private val _proceedToConfirm = SingleLiveEvent<Resource<Unit>>()

    val data: LiveData<Resource<BundlelistParent>> get() = _data
    private val _data = MutableLiveData<Resource<BundlelistParent>>()


    val dataSelf: LiveData<Resource<Params>> get() = _dataSelf
    private val _dataSelf = MutableLiveData<Resource<Params>>()

    val submitted: LiveData<Resource<SubmitResult>> get() = _submitted
    private val _submitted = SingleLiveEvent<Resource<SubmitResult>>()


    fun proceed(
        type:Int,
        number: String,
        bundleName: String,
        remark: String,
        transactionAmount: String,
        idValue: String,
        idType: String,
        bundle: String,
        validity:String
    ) {

        _dataSelf.emitSuccess(
            Params(
                type,
                number,
                bundleName,
                remark,
                transactionAmount,
                idValue,
                idType,
                bundle,
                validity
            )
        )
        _proceedToConfirm.emitSuccess(Unit)
    }

    /*fun proceedothers(number: String, amount: Double) {
        _data.emitSuccess(DataBundle(number, amount.toString()))
        _proceedToConfirm.emitSuccess(Unit)
    }*/


    fun getServices(currency:String) {

        execute(
            getAirtimeData,
            currency,
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



    fun getConfirmationSelf(
        remark: String,
        transactionAmount: String,
        pin: String,
        idValue: String,
        idType: String,
        bundle: String,
    ) {

        execute(
            confirmationselfUseCase,
            BundleSelfConfirmationUseCase.Params(
                remark,
                transactionAmount,
                pin,
                idValue,
                idType,
                bundle
            ),
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

    fun getConfirmationOther(
        number: String,
        transactionAmount: String,
        pin: String,
        idValue: String,
        idType: String,
        bundle: String,
    ) {

        execute(
            confirmationotherUseCase,
            BundleOtherConfirmationUseCase.Params(
                number,
                transactionAmount,
                pin,
                idValue,
                idType,
                bundle
            ),
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