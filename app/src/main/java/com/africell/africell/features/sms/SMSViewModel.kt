package com.africell.africell.features.sms

import androidx.lifecycle.MutableLiveData
import com.africell.africell.app.AppSessionNavigator
import com.africell.africell.app.ResourceUseCaseExecutor
import com.africell.africell.data.Resource
import com.africell.africell.data.SingleLiveEvent
import com.africell.africell.data.api.dto.SMSCountDTO
import com.africell.africell.data.entity.SMSData
import com.africell.africell.exception.AppExceptionFactory
import com.africell.africell.features.sms.domain.GetSMSDatatUseCase
import com.africell.africell.features.sms.domain.SendFreeSmsUseCase
import com.africell.africell.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
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
