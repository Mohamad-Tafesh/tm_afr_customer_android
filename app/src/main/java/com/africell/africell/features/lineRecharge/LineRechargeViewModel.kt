package com.africell.africell.features.lineRecharge

import androidx.lifecycle.MutableLiveData
import com.africell.africell.app.AppSessionNavigator
import com.africell.africell.app.ResourceUseCaseExecutor
import com.tedmob.afrimoney.data.Resource
import com.africell.africell.data.SingleLiveEvent
import com.africell.africell.data.api.dto.RechargeCardDTO
import com.africell.africell.data.api.dto.SMSCountDTO
import com.africell.africell.data.entity.Country
import com.africell.africell.exception.AppExceptionFactory
import com.africell.africell.features.authentication.domain.GetCountriesUseCase
import com.africell.africell.features.lineRecharge.domain.GetRechargeCardUseCase
import com.africell.africell.features.lineRecharge.domain.VoucherRechargeUseCase
import com.africell.africell.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LineRechargeViewModel
@Inject constructor(
    private val getCountries: GetCountriesUseCase,
    private val getRechargeCardUseCase: GetRechargeCardUseCase,
    private val voucherRechargeUseCase: VoucherRechargeUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {

    val cardsData = MutableLiveData<Resource<List<RechargeCardDTO>>>()
    val rechargeVoucherData = SingleLiveEvent<Resource<SMSCountDTO>>()
    val countriesData = MutableLiveData<Resource<List<Country>>>()

    fun getCountries() {
        ResourceUseCaseExecutor(
            getCountries,
            Unit,
            countriesData,
            appExceptionFactory,
            appSessionNavigator,
            null
        ).execute()
    }

    fun getRechargeCards() {
        ResourceUseCaseExecutor(getRechargeCardUseCase, Unit, cardsData,appExceptionFactory, appSessionNavigator) {
            getRechargeCards()
        }.execute()
    }

    fun rechargeVoucher(receiverMsisdn: String?, voucher: String) {
        ResourceUseCaseExecutor(
            voucherRechargeUseCase,
            VoucherRechargeUseCase.Params(receiverMsisdn, voucher),
            rechargeVoucherData,
            appExceptionFactory,appSessionNavigator
        ).execute()
    }

    override fun onCleared() {
        voucherRechargeUseCase.dispose()
        getRechargeCardUseCase.dispose()
        getCountries.dispose()
        super.onCleared()
    }
}
