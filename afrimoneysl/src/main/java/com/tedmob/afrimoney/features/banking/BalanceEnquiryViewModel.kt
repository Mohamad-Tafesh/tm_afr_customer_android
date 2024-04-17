package com.tedmob.afrimoney.features.banking

import androidx.lifecycle.LiveData
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.data.Resource
import com.tedmob.afrimoney.data.SingleLiveEvent
import com.tedmob.afrimoney.data.entity.Bank
import com.tedmob.afrimoney.data.entity.SubmitResult
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.features.banking.domain.GetBankBalanceEnquiryUseCase
import com.tedmob.afrimoney.features.banking.domain.GetBanksUseCase
import com.tedmob.afrimoney.features.banking.domain.SubmitBankToWalletDataUseCase
import com.tedmob.afrimoney.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BalanceEnquiryViewModel
@Inject constructor(
    private val submit: GetBankBalanceEnquiryUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator,
) : BaseViewModel() {
    val submitted: LiveData<Resource<SubmitResult>> get() = _submitted
    private val _submitted = SingleLiveEvent<Resource<SubmitResult>>()

    fun confirm(
        bankID: String,
        accNb: String,
        pin: String
    ) {
        executeResource(
            submit,
            GetBankBalanceEnquiryUseCase.Params(bankID,accNb, pin),
            _submitted,
            appExceptionFactory,
            appSessionNavigator,
            null
        )
    }

}