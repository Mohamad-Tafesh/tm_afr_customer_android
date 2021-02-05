package com.tedmob.africell.features.sms

import androidx.lifecycle.MutableLiveData
import com.tedmob.africell.app.AppSessionNavigator
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.SingleLiveEvent
import com.tedmob.africell.data.api.dto.SMSCountDTO
import com.tedmob.africell.data.entity.SMSData
import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.sms.domain.GetSMSDatatUseCase
import com.tedmob.africell.features.sms.domain.SendFreeSmsUseCase
import com.tedmob.africell.ui.BaseViewModel
import javax.inject.Inject

class SMSViewModel
@Inject constructor(
    private val getSMSDatatUseCase: GetSMSDatatUseCase,
    private val sendFreeSmsUseCase: SendFreeSmsUseCase,
        private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {

    val smsData = MutableLiveData<Resource<SMSData>>()
    val smsSentData = SingleLiveEvent<Resource<SMSCountDTO>>()

    fun getSmsCount() {
        ResourceUseCaseExecutor(getSMSDatatUseCase, Unit, smsData,appExceptionFactory, appSessionNavigator) {
            getSmsCount()
        }.execute()
    }

    fun sendSMS(receiverMsisdn: String?, message: String) {
        ResourceUseCaseExecutor(
            sendFreeSmsUseCase,
            SendFreeSmsUseCase.Params(receiverMsisdn, message),
            smsSentData,
            appExceptionFactory,appSessionNavigator
        ).execute()
    }

    override fun onCleared() {
        sendFreeSmsUseCase.dispose()
        getSMSDatatUseCase.dispose()
        super.onCleared()
    }
}
