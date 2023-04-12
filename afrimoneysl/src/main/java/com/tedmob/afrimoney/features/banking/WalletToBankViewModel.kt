package com.tedmob.afrimoney.features.banking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.app.handleInvalidSession
import com.tedmob.afrimoney.data.*
import com.tedmob.afrimoney.data.entity.Bank
import com.tedmob.afrimoney.data.entity.BankAccount
import com.tedmob.afrimoney.data.entity.BankAccountId
import com.tedmob.afrimoney.data.entity.SubmitResult
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.data.entity.GetFeesData
import com.tedmob.afrimoney.features.banking.domain.*
import com.tedmob.afrimoney.ui.BaseViewModel
import com.tedmob.afrimoney.util.security.StringEncryptor
import com.tedmob.afrimoney.util.security.encrypted
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@HiltViewModel
class WalletToBankViewModel
@Inject constructor(
    private val getFeesUseCase: GetFeesWalletUseCase,
    private val submit: SubmitWalletToBankDataUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator,
    @Named("local-string") private val encryptor: StringEncryptor,
) : BaseViewModel() {

    var selectedBankId: String? by object : ReadWriteProperty<Any?, String?> {
        private var value by encrypted(encryptor, "")

        override fun getValue(thisRef: Any?, property: KProperty<*>): String? {
            return this.value.takeIf { it.isNotEmpty() }
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: String?) {
            this.value = value.orEmpty()
        }
    }

    private var selectedBankAccountId: BankAccountId? by object :
        ReadWriteProperty<Any?, BankAccountId?> {
        private var value by encrypted(encryptor, "")

        override fun getValue(thisRef: Any?, property: KProperty<*>): BankAccountId? {
            return this.value.takeIf { it.isNotEmpty() }?.let { BankAccountId(it) }
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: BankAccountId?) {
            this.value = value?.value.orEmpty()
        }
    }
    private var name: String by encrypted(encryptor, "")
    private var accNumber: String by encrypted(encryptor, "")
    private var amount: String by encrypted(encryptor, "")
    private var wallet: String by encrypted(encryptor, "")

    private var total: String by encrypted(encryptor, "")


    val proceedToConfirm: LiveData<Resource<Unit>> get() = _proceedToConfirm
    private val _proceedToConfirm = SingleLiveEvent<Resource<Unit>>()

    val data: LiveData<Resource<GetFeesData>> get() = _data
    private val _data = MutableLiveData<Resource<GetFeesData>>()

    val submitted: LiveData<Resource<SubmitResult>> get() = _submitted
    private val _submitted = SingleLiveEvent<Resource<SubmitResult>>()


    fun proceed(wallet: String, amount: Double) {
        this.wallet = wallet
        this.amount = amount.toBigDecimal().toPlainString()

        _proceedToConfirm.emitSuccess(Unit)
    }

    fun getFees(bank: Bank, amount: String) {
        execute(
            getFeesUseCase,
            GetFeesWalletUseCase.Params(
                bank.bankid,
                bank.accnum, amount
            ),
            onLoading = {
                _data.emitLoading()
            },
            onSuccess = {
                val data = GetFeesData(it.number, it.amount, it.fees, null, it.total)

                _data.emitSuccess(data)
            },
            onError = {
                val exception = appExceptionFactory.make(it)
                if (!exception.handleInvalidSession(appSessionNavigator)) {
                    _data.emitError(exception.userMessage, action = { getFees(bank, amount) })
                }
            }
        )
    }

    fun confirm(
        bankNumber: String,
        bankID: String,
        pin: String
    ) {
        executeResource(
            submit,
            SubmitWalletToBankDataUseCase.Params(
                amount,pin, bankID, bankNumber
            ),
            _submitted,
            appExceptionFactory,
            appSessionNavigator,
            null
        )
    }


    fun clear() {
        onCleared()
    }

    override fun onCleared() {
        super.onCleared()
        selectedBankId = null
        selectedBankAccountId = null
        name = ""
        accNumber = ""
        amount = ""
        total = ""
    }
}