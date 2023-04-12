package com.tedmob.afrimoney.features.bills.fcc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.app.handleInvalidSession
import com.tedmob.afrimoney.data.*
import com.tedmob.afrimoney.data.entity.SubmitResult
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.features.bills.fcc.domain.GetAmountFccUseCase
import com.tedmob.afrimoney.features.bills.fcc.domain.SubmitFccUseCase
import com.tedmob.afrimoney.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FccViewModel
@Inject constructor(
    private val getAmountUseCase: GetAmountFccUseCase,
    private val confirmationUseCase: SubmitFccUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator,

    ) : BaseViewModel() {

    private var fName:String? = null
    private var lName:String? = null
    private var adress:String? = null

    val proceedToConfirm: LiveData<Resource<Unit>> get() = _proceedToConfirm
    private val _proceedToConfirm = SingleLiveEvent<Resource<Unit>>()

    val data: LiveData<Resource<FccData>> get() = _data
    private val _data = MutableLiveData<Resource<FccData>>()

    val submitted: LiveData<Resource<SubmitResult>> get() = _submitted
    private val _submitted = SingleLiveEvent<Resource<SubmitResult>>()


    fun proceed(fName:String,lName:String,adress:String) {

        _proceedToConfirm.emitSuccess(Unit)
        this.fName=fName
        this.lName=lName
        this.adress=adress

        _proceedToConfirm.emitSuccess(Unit)

    }

    data class FccData(val fName:String,val lName:String,val adress:String,val tax:String)


    fun getAmount() {

        execute(
            getAmountUseCase,
            Unit,
            onLoading = {
                _data.emitLoading()
            },
            onSuccess = {

                _data.emitSuccess(FccData(fName.orEmpty(),lName.orEmpty(),adress.orEmpty(), it))
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
           (pin),
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