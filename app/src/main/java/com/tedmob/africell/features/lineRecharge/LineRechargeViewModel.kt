package com.tedmob.africell.features.lineRecharge

import androidx.lifecycle.MutableLiveData
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.SingleLiveEvent
import com.tedmob.africell.data.api.dto.RechargeCardDTO
import com.tedmob.africell.data.api.dto.SMSCountDTO
import com.tedmob.africell.data.entity.SMSData
import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.lineRecharge.domain.GetRechargeCardUseCase
import com.tedmob.africell.features.lineRecharge.domain.VoucherRechargeUseCase
import com.tedmob.africell.features.sms.domain.SendFreeSmsUseCase
import com.tedmob.africell.ui.BaseViewModel
import javax.inject.Inject

class LineRechargeViewModel
@Inject constructor(
    private val getRechargeCardUseCase: GetRechargeCardUseCase,
    private val voucherRechargeUseCase: VoucherRechargeUseCase,
    private val appExceptionFactory: AppExceptionFactory
) : BaseViewModel() {

    val cardsData = MutableLiveData<Resource<List<RechargeCardDTO>>>()
    val rechargeVoucherData = SingleLiveEvent<Resource<SMSCountDTO>>()

    fun getRechargeCards() {
        ResourceUseCaseExecutor(getRechargeCardUseCase, Unit, cardsData, appExceptionFactory) {
            getRechargeCards()
        }.execute()
    }

    fun rechargeVoucher(receiverMsisdn: String?, voucher: String) {
        ResourceUseCaseExecutor(
            voucherRechargeUseCase,
            VoucherRechargeUseCase.Params(receiverMsisdn, voucher),
            rechargeVoucherData,
            appExceptionFactory
        ).execute()
    }

    override fun onCleared() {
        voucherRechargeUseCase.dispose()
        getRechargeCardUseCase.dispose()
        super.onCleared()
    }
}
