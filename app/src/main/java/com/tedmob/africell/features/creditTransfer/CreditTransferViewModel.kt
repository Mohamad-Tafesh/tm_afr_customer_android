package com.tedmob.africell.features.creditTransfer

import com.tedmob.africell.app.AppSessionNavigator
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.SingleLiveEvent
import com.tedmob.africell.data.api.dto.SMSCountDTO
import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.creditTransfer.domain.CreditTransferUseCase
import com.tedmob.africell.ui.BaseViewModel
import javax.inject.Inject

class CreditTransferViewModel
@Inject constructor(
    private val creditTransferUseCase: CreditTransferUseCase,
        private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {


    val creditTransferData = SingleLiveEvent<Resource<SMSCountDTO>>()

    fun creditTransfer(receiverMsisdn: String?, voucher: String) {
        ResourceUseCaseExecutor(
            creditTransferUseCase,
            CreditTransferUseCase.Params(receiverMsisdn, voucher),
            creditTransferData,
            appExceptionFactory,appSessionNavigator
        ).execute()
    }

    override fun onCleared() {
        creditTransferUseCase.dispose()
        super.onCleared()
    }
}
