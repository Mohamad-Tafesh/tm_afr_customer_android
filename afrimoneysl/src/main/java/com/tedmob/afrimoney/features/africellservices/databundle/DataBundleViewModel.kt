package com.tedmob.afrimoney.features.africellservices.databundle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.app.handleInvalidSession
import com.tedmob.afrimoney.data.*
import com.tedmob.afrimoney.data.entity.SubmitResult
import com.tedmob.afrimoney.data.repository.domain.SessionRepository
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.features.africellservices.databundle.domain.BundleOtherConfirmationUseCase
import com.tedmob.afrimoney.features.africellservices.databundle.domain.BundleSelfConfirmationUseCase
import com.tedmob.afrimoney.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DataBundleViewModel
@Inject constructor(
    private val confirmationselfUseCase: BundleSelfConfirmationUseCase,
    private val confirmationotherUseCase: BundleOtherConfirmationUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val session: SessionRepository,
    private val appSessionNavigator: AppSessionNavigator,

    ) : BaseViewModel() {

    class Params(
        val type: Int,
        val number: String,
        val bundleName: String,
        val remark: String,
        val transactionAmount: String,
        val idValue: String,
        val idType: String,
        val bundle: String,
        val validity: String,
        val walletID: String
    )

    data class DataBundle(
        val number: String,
        val amount: String,
    )

    val proceedToConfirm: LiveData<Resource<Unit>> get() = _proceedToConfirm
    private val _proceedToConfirm = SingleLiveEvent<Resource<Unit>>()

    val dataSelf: LiveData<Resource<Params>> get() = _dataSelf
    private val _dataSelf = MutableLiveData<Resource<Params>>()

    val dataAmount: LiveData<Resource<List<String>>> get() = _dataAmount
    private val _dataAmount = MutableLiveData<Resource<List<String>>>()

    val submitted: LiveData<Resource<SubmitResult>> get() = _submitted
    private val _submitted = SingleLiveEvent<Resource<SubmitResult>>()


    fun proceed(
        type: Int,
        number: String,
        bundleName: String,
        remark: String,
        transactionAmount: String,
        idValue: String,
        idType: String,
        bundle: String,
        validity: String,
        walletID: String
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
                validity,
                walletID
            )
        )
        _proceedToConfirm.emitSuccess(Unit)
    }

    /*fun proceedothers(number: String, amount: Double) {
        _data.emitSuccess(DataBundle(number, amount.toString()))
        _proceedToConfirm.emitSuccess(Unit)
    }*/


    fun getConfirmationSelf(
        remark: String,
        transactionAmount: String,
        pin: String,
        idValue: String,
        idType: String,
        bundle: String,
        walletID: String,
    ) {

        execute(
            confirmationselfUseCase,
            BundleSelfConfirmationUseCase.Params(
                remark,
                transactionAmount,
                pin,
                idValue,
                idType,
                bundle,
                walletID
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
        walletID: String,
    ) {

        execute(
            confirmationotherUseCase,
            BundleOtherConfirmationUseCase.Params(
                number,
                transactionAmount,
                pin,
                idValue,
                idType,
                bundle,
                walletID
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