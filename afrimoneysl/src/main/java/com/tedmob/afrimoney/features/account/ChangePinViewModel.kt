package com.tedmob.afrimoney.features.account

import androidx.lifecycle.LiveData
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.app.handleInvalidSession
import com.tedmob.afrimoney.data.*
import com.tedmob.afrimoney.data.entity.SubmitResult
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.features.account.domain.ChangePinUseCase
import com.tedmob.afrimoney.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangePinViewModel
@Inject constructor(
    private val confirmationUseCase: ChangePinUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator,
) : BaseViewModel() {


    val pinChanged: LiveData<Resource<SubmitResult>> get() = _pinChanged
    private val _pinChanged = SingleLiveEvent<Resource<SubmitResult>>()

    lateinit var oldpin: String
    fun getConfirmation(oldpin: String, newpin: String) {

        execute(
            confirmationUseCase,
            ChangePinUseCase.Params(oldpin, newpin),
            onLoading = {
                _pinChanged.emitLoading()
            },
            onSuccess = {

                _pinChanged.emitSuccess(it)
            },
            onError = {
                val exception = appExceptionFactory.make(it)
                if (!exception.handleInvalidSession(appSessionNavigator)) {
                    _pinChanged.emitError(exception.userMessage)
                }
            }
        )
    }



    override fun onCleared() {
        super.onCleared()
        //...
    }
}