package com.tedmob.afrimoney.features.bills.covid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.app.handleInvalidSession
import com.tedmob.afrimoney.data.*
import com.tedmob.afrimoney.data.entity.SubmitResult
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.features.bills.covid.domain.GetAmountTravelUseCase
import com.tedmob.afrimoney.features.bills.covid.domain.SubmitCovidUseCase
import com.tedmob.afrimoney.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CovidViewModel
@Inject constructor(
    private val getAmountUseCase: GetAmountTravelUseCase,
    private val confirmationUseCase: SubmitCovidUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator,

    ) : BaseViewModel() {

    private var amount:String? = null
    private var number:String? = null

    val proceedToConfirm: LiveData<Resource<Unit>> get() = _proceedToConfirm
    private val _proceedToConfirm = SingleLiveEvent<Resource<Unit>>()

    val data: LiveData<Resource<CovidData>> get() = _data
    private val _data = MutableLiveData<Resource<CovidData>>()

    val submitted: LiveData<Resource<SubmitResult>> get() = _submitted
    private val _submitted = SingleLiveEvent<Resource<SubmitResult>>()


    data class CovidData(val number:String,val amount:String)


    fun getAmount() {

        execute(
            getAmountUseCase,
            Unit,
            onLoading = {

                _proceedToConfirm.emitLoading()
                _data.emitLoading()
            },
            onSuccess = {

                _proceedToConfirm.emitSuccess(Unit)
                _data.emitSuccess(CovidData(number.orEmpty(),it))
            },
            onError = {
                val exception = appExceptionFactory.make(it)
                if (!exception.handleInvalidSession(appSessionNavigator)) {
                    _data.emitError(exception.userMessage)
                    _proceedToConfirm.emitError(exception.userMessage)
                }
            }
        )
    }

    fun getCancelResult(pin:String) {

        execute(
            confirmationUseCase,
            SubmitCovidUseCase.Params(number.orEmpty(), amount.orEmpty(),pin,"cancel"),
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

    fun getConfirmResult(pin:String) {

        execute(
            confirmationUseCase,
            SubmitCovidUseCase.Params(number.orEmpty(), amount.orEmpty(),pin,"confirm"),
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